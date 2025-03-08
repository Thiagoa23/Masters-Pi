package com.masterspi.service;

import com.masterspi.model.Produto;
import com.masterspi.repository.ProdutoRepository;
import com.masterspi.strategy.ImagemStorageStrategy;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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

    public Page<Produto> listarProdutos(String nome, int pagina) {
        Pageable pageable = PageRequest.of(pagina - 1, 10, Sort.by(Sort.Direction.DESC, "codigo"));

        if (nome != null && !nome.isEmpty()) {
            return produtoRepository.findByNomeContainingIgnoreCase(nome, pageable);
        }
        return produtoRepository.findAll(pageable);
    }

    public Produto salvarProduto(String nome, double avaliacao, String descricao, BigDecimal valor, int estoque, boolean ativo, MultipartFile[] imagens, String imagemPrincipal) {
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setAvaliacao(avaliacao);
        produto.setDescricao(descricao);
        produto.setValor(valor);
        produto.setEstoque(estoque);
        produto.setAtivo(ativo);

        List<String> caminhosImagens = new ArrayList<>();

        for (MultipartFile imagem : imagens) {
            try {
                String nomeArquivo = imagem.getOriginalFilename(); // Obtém o nome do arquivo
                byte[] bytesImagem = imagem.getBytes(); // Converte para byte[]

                String caminho = imagemStorageStrategy.salvarImagem(nomeArquivo, bytesImagem).toString(); // Converte Path para String
                caminhosImagens.add(caminho);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao salvar imagem", e);
            }
        }

        produto.setImagens(caminhosImagens);
        produto.setImagemPrincipal(imagemPrincipal);

        return produtoRepository.save(produto);
    }

}
