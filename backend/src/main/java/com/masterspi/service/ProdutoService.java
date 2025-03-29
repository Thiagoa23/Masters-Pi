package com.masterspi.service;

import com.masterspi.model.Produto;
import com.masterspi.repository.ProdutoRepository;
import com.masterspi.strategy.ImagemStorageStrategy;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProdutoService {

    private ProdutoRepository produtoRepository;
    private final ImagemStorageStrategy imagemStorageStrategy;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository, ImagemStorageStrategy imagemStorageStrategy) {
        this.produtoRepository = produtoRepository;
        this.imagemStorageStrategy = imagemStorageStrategy;
    }

    public Optional<Produto> buscarPorCodigo(Long codigo) {
        return produtoRepository.findById(codigo);
    }

    public Page<Produto> listarProdutos(String nome, int pagina) {
        Pageable pageable = PageRequest.of(pagina - 1, 10, Sort.by(Sort.Direction.DESC, "codigo"));

        if (nome != null && !nome.isEmpty()) {
            return produtoRepository.findByNomeContainingIgnoreCase(nome, pageable);
        }
        return produtoRepository.findAll(pageable);
    }

    public Produto save(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Produto salvarProduto(
            String nome,
            double avaliacao,
            String descricao,
            BigDecimal valor,
            int estoque,
            boolean ativo,
            MultipartFile[] imagens,
            String nomeImagemPrincipal
    ) {
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setAvaliacao(avaliacao);
        produto.setDescricao(descricao);
        produto.setValor(valor);
        produto.setEstoque(estoque);
        produto.setAtivo(ativo);

        List<String> caminhosImagens = new ArrayList<>();

        if (imagens != null) {
            for (MultipartFile imagem : imagens) {
                if (!imagem.isEmpty()) {
                    try {
                        String nomeArquivoOriginal = imagem.getOriginalFilename();
                        byte[] bytesImagem = imagem.getBytes();

                        // Salva no disco (gera "uploads/uuid_nomeArquivoOriginal")
                        String caminhoSalvo = imagemStorageStrategy
                                .salvarImagem(nomeArquivoOriginal, bytesImagem)
                                .toString();

                        caminhosImagens.add(caminhoSalvo);

                        // Se o usuario selecionou esta como principal
                        // (comparando com "contains" se preferir lidar com espaços)
                        if (nomeImagemPrincipal != null
                                && nomeImagemPrincipal.length() > 0
                                && caminhoSalvo.contains(nomeImagemPrincipal)) {
                            produto.setImagemPrincipal(caminhoSalvo);
                        }

                    } catch (IOException e) {
                        throw new RuntimeException("Erro ao salvar imagem", e);
                    }
                }
            }
        }

        produto.setImagens(caminhosImagens);

        // Se o usuario informou nomeImagemPrincipal e ainda nao definimos
        // a principal (pode ser que não tenha batido no 'contains' acima),
        // pode ser que não existam imagens ou o nome não bateu:
        if (nomeImagemPrincipal != null
                && !nomeImagemPrincipal.isEmpty()
                && produto.getImagemPrincipal() == null) {
            // Tenta filtrar
            String imagemPrincipal = caminhosImagens.stream()
                    .filter(path -> path.contains(nomeImagemPrincipal))
                    .findFirst()
                    .orElse(null);
            produto.setImagemPrincipal(imagemPrincipal);
        }

        return produtoRepository.save(produto);
    }

    /**
     * Edita um produto existente (alterar).
     */
    public Produto alterarProduto(
            Long codigo,
            String nome,
            double avaliacao,
            String descricao,
            BigDecimal valor,
            int estoque,
            List<MultipartFile> novasImagens,
            String nomeImagemPrincipal // Valor do radio; pode ser vazio se o usuário não alterou
    ) {
        Produto produto = produtoRepository.findById(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        // Atualiza campos básicos
        produto.setNome(nome);
        produto.setAvaliacao(avaliacao);
        produto.setDescricao(descricao);
        produto.setValor(valor);
        produto.setEstoque(estoque);

        // Copia a lista atual de imagens (antigas)
        List<String> listaAtual = new ArrayList<>(produto.getImagens());

        // Flag para indicar se definimos uma nova imagem principal
        boolean principalAtualizada = false;

        // Processa novas imagens, se houver
        if (novasImagens != null && !novasImagens.isEmpty()) {
            for (MultipartFile imagem : novasImagens) {
                if (!imagem.isEmpty()) {
                    try {
                        String nomeArquivoOriginal = imagem.getOriginalFilename();
                        byte[] bytesImagem = imagem.getBytes();

                        // Salva a imagem e obtém o caminho salvo (geralmente algo como "uploads/uuid_nomeArquivo.jpeg")
                        String caminhoSalvo = imagemStorageStrategy
                                .salvarImagem(nomeArquivoOriginal, bytesImagem)
                                .toString();

                        listaAtual.add(caminhoSalvo);

                        // Se o usuário escolheu esta imagem nova como principal, 
                        // o valor do radio (nomeImagemPrincipal) deve ser igual ao nome original.
                        if (nomeImagemPrincipal != null
                                && !nomeImagemPrincipal.isEmpty()
                                && nomeImagemPrincipal.equals(nomeArquivoOriginal)) {
                            produto.setImagemPrincipal(caminhoSalvo);
                            principalAtualizada = true;
                        }

                    } catch (IOException e) {
                        throw new RuntimeException("Erro ao salvar imagem", e);
                    }
                }
            }
        }

        // Atualiza a lista de imagens do produto (antigas + novas)
        produto.setImagens(listaAtual);

        // Se o usuário enviou um valor para imagem principal, mas nenhuma nova imagem foi marcada,
        // tenta atualizar usando uma das imagens antigas.
        if (nomeImagemPrincipal != null && !nomeImagemPrincipal.isEmpty() && !principalAtualizada) {
            // Procura por uma imagem antiga cujo caminho contenha o valor enviado.
            // Essa comparação usa contains para lidar com diferenças como a adição de UUID.
            String imagemPrincipalAntiga = listaAtual.stream()
                    .filter(img -> img.contains(nomeImagemPrincipal))
                    .findFirst()
                    .orElse(null);

            // Se encontrar, atualiza a imagem principal.
            if (imagemPrincipalAntiga != null) {
                produto.setImagemPrincipal(imagemPrincipalAntiga);
            }
        }
        // Se o valor de nomeImagemPrincipal estiver vazio, não altera a imagem principal (mantém a atual).

        return produtoRepository.save(produto);
    }

    public void alterarStatusProduto(Long codigo) {
        Produto produto = produtoRepository.findById(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        produto.setAtivo(!produto.isAtivo()); // Alterna o status
        produtoRepository.save(produto); // Salva a alteração no banco
    }

}
