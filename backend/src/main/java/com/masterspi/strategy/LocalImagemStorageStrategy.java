/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
            if (!Files.exists(diretorio)) {
                Files.createDirectories(diretorio);
            }

            Path caminhoArquivo = diretorio.resolve(nomeArquivo);
            Files.write(caminhoArquivo, dadosImagem);
            return caminhoArquivo;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar imagem", e);
        }
    }

    @Override
    public void deletarImagem(String nomeArquivo) {
        try {
            Path caminhoArquivo = Paths.get(DIRETORIO_IMAGENS, nomeArquivo);
            Files.deleteIfExists(caminhoArquivo);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao deletar imagem", e);
        }
    }

    @Override
    public Path obterCaminhoImagem(String nomeArquivo) {
        return Paths.get(DIRETORIO_IMAGENS, nomeArquivo);
    }
}
