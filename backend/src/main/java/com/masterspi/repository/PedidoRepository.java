/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.masterspi.repository;

import com.masterspi.model.Pedido;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Thiago
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT COALESCE(MAX(p.numeroPedido), 0) FROM Pedido p")
    Integer buscarUltimoNumeroPedido();
    
    List<Pedido> findByClienteIdOrderByDataCriacaoDesc(Long clienteId);

    List<Pedido> findAllByOrderByDataCriacaoDesc();
}

