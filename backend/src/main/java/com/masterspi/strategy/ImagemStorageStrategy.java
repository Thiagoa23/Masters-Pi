/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.masterspi.strategy;

import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author andra
 */
public interface ImagemStorageStrategy {

    Path salvarImagem(String nomeArquivo, byte[] dadosImagem) throws IOException;

    void deletarImagem(String nomeArquivo) throws IOException;    // se também quiser permitir exceção

    Path obterCaminhoImagem(String nomeArquivo) throws IOException; // idem
}
