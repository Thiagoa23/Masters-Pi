package com.masterspi.controller;

import com.masterspi.model.Produto;
import com.masterspi.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoApiController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping("/ativos")
    public List<Produto> listarProdutosAtivos() {
        return produtoService.buscarProdutosAtivos();
    }
    
    @GetMapping("/{codigo}")
    public ResponseEntity<Produto> buscarPorCodigo(@PathVariable Long codigo) {
        return produtoService.buscarPorCodigo(codigo)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
