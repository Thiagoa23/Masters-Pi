package com.masterspi.strategy;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class LocalImagemStorageStrategy implements ImagemStorageStrategy {

    // Diretório relativo no projeto onde as imagens serão salvas
    private final String uploadDir = "uploads";

    @Override
    public Path salvarImagem(String nomeArquivo, byte[] conteudo) throws IOException {
        Path pastaUploads = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(pastaUploads);

        // Remove ou substitui espaços e caracteres problemáticos
        String nomeArquivoSanitizado = nomeArquivo.replaceAll("[^a-zA-Z0-9.\\-]", "_");

        // Gera um nome único (UUID + nome sanitizado)
        String novoNomeArquivo = UUID.randomUUID().toString() + "_" + nomeArquivoSanitizado;

        // Caminho completo no sistema de arquivos
        Path caminhoArquivo = pastaUploads.resolve(novoNomeArquivo);

        Files.write(caminhoArquivo, conteudo);

        // Retorna apenas o NOME do arquivo, sem "uploads/"
        return Paths.get(novoNomeArquivo);
    }

    @Override
    public void deletarImagem(String nomeArquivo) {
        try {
            // Obter o caminho absoluto do arquivo, considerando que nomeArquivo pode ser "uploads/uuid_nomeOriginal.ext"
            Path pastaUploads = Paths.get(uploadDir).toAbsolutePath().normalize();
            // Se o nomeArquivo já incluir "uploads", remova-o para evitar duplicidade
            String nomeArquivoLimpo = nomeArquivo.replaceFirst("^uploads[/\\\\]", "");
            Path caminhoArquivo = pastaUploads.resolve(nomeArquivoLimpo);
            Files.deleteIfExists(caminhoArquivo);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao deletar imagem: " + e.getMessage(), e);
        }
    }

    @Override
    public Path obterCaminhoImagem(String nomeArquivo) {
        // Se o nomeArquivo já incluir o diretório "uploads", pode ser retornado diretamente ou resolvido
        Path pastaUploads = Paths.get(uploadDir).toAbsolutePath().normalize();
        String nomeArquivoLimpo = nomeArquivo.replaceFirst("^uploads[/\\\\]", "");
        return pastaUploads.resolve(nomeArquivoLimpo);
    }

}
