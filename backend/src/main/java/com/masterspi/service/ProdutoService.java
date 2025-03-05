package com.masterspi.service;

import com.masterspi.model.Produto;
import com.masterspi.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public Page<Produto> listarProdutos(String nome, int pagina) {
        Pageable pageable = PageRequest.of(pagina - 1, 10, Sort.by(Sort.Direction.DESC, "codigo"));
        
        if (nome != null && !nome.isEmpty()) {
            return produtoRepository.findByNomeContainingIgnoreCase(nome, pageable);
        }
        return produtoRepository.findAll(pageable);
    }
}
