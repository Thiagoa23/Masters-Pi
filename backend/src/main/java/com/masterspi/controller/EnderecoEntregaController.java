/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.masterspi.controller;

import com.masterspi.model.EnderecoEntrega;
import com.masterspi.repository.EnderecoEntregaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Thiago
 */
@RestController
public class EnderecoEntregaController {

    @Autowired
    private EnderecoEntregaRepository enderecoEntregaRepository;

    @GetMapping("/api/cliente/{id}/enderecos")
    public ResponseEntity<List<EnderecoEntrega>> listarEnderecosPorCliente(@PathVariable Long id) {
        List<EnderecoEntrega> enderecos = enderecoEntregaRepository.findByClienteId(id);
        return ResponseEntity.ok(enderecos);
    }
}
