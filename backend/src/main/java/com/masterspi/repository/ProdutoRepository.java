package com.masterspi.repository;

import com.masterspi.model.Produto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Page<Produto> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
    
    List<Produto> findByAtivoTrueOrderByCodigoDesc();

    Page<Produto> findByAtivoTrue(Pageable pageable);
    
    List<Produto> findByAtivoTrue();
}
