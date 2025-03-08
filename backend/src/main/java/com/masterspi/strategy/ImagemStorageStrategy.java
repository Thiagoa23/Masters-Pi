/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.masterspi.strategy;

import java.nio.file.Path;

/**
 *
 * @author andra
 */
public interface ImagemStorageStrategy {
    Path salvarImagem(String nomeArquivo, byte[] dadosImagem);
    void deletarImagem(String nomeArquivo);
    Path obterCaminhoImagem(String nomeArquivo);
}
