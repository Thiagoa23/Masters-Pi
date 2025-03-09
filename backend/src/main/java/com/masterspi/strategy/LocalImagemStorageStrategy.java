package com.masterspi.strategy;

import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.*;

/**
 *
 * @author andra
 */
@Component
public class LocalImagemStorageStrategy implements ImagemStorageStrategy {

    private static final String DIRETORIO_IMAGENS = "imagens/produtos/";

    @Override
    public Path salvarImagem(String nomeArquivo, byte[] dadosImagem) {
        try {
            Path diretorio = Paths.get(DIRETORIO_IMAGENS);

            // Garante que o diretório existe antes de salvar a imagem
            Files.createDirectories(diretorio);

            Path caminhoArquivo = diretorio.resolve(nomeArquivo);

            // Verifica se o arquivo já existe e gera um novo nome se necessário
            if (Files.exists(caminhoArquivo)) {
                caminhoArquivo = diretorio.resolve(System.currentTimeMillis() + "_" + nomeArquivo);
            }

            // Salva o arquivo
            Files.write(caminhoArquivo, dadosImagem, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            return caminhoArquivo;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar imagem: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletarImagem(String nomeArquivo) {
        try {
            Path caminhoArquivo = Paths.get(DIRETORIO_IMAGENS, nomeArquivo);

            // Verifica se o arquivo existe antes de tentar deletar
            if (Files.exists(caminhoArquivo)) {
                Files.delete(caminhoArquivo);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao deletar imagem: " + e.getMessage(), e);
        }
    }

    @Override
    public Path obterCaminhoImagem(String nomeArquivo) {
        return Paths.get(DIRETORIO_IMAGENS, nomeArquivo);
    }
}
