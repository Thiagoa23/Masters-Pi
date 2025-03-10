package com.masterspi.controller;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/imagens")
public class ImagemController {

    private final Path pastaUploads = Paths.get("uploads").toAbsolutePath();

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> carregarImagem(@PathVariable String filename) throws MalformedURLException {
        // Monta o caminho absoluto no disco
        Path caminhoArquivo = pastaUploads.resolve(filename).normalize();

        // Cria um Resource apontando para o arquivo
        Resource resource = new UrlResource(caminhoArquivo.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            // Se não existir ou não puder ser lido, retorne 404
            return ResponseEntity.notFound().build();
        }

        // Opcional: definir o content-type (image/png, image/jpeg etc.)
        return ResponseEntity.ok(resource);
    }
}
