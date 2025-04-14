package com.masterspi.controller;

import com.masterspi.model.Cliente;
import com.masterspi.model.EnderecoEntrega;
import com.masterspi.service.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarCliente(@RequestBody Cliente cliente) {
        try {
            clienteService.cadastrarCliente(cliente);
            return ResponseEntity.ok("Cadastro realizado com sucesso. Você será redirecionado para a tela de login.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<Cliente> buscarClientePorEmail(@RequestParam String email) {
        return clienteService.buscarPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Cliente loginRequest, HttpSession session) {
        try {
            Cliente clienteLogado = clienteService.loginCliente(loginRequest.getEmail(), loginRequest.getSenha());
            session.setAttribute("clienteLogado", clienteLogado);
            return ResponseEntity.ok("Login realizado com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro no login: " + e.getMessage());
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        System.out.println("Sessão atual: " + session);
        System.out.println("Atributo clienteLogado: " + session.getAttribute("clienteLogado"));
        if (session.getAttribute("clienteLogado") != null) {
            session.invalidate();
            return ResponseEntity.ok("Logout realizado com sucesso.");
        } else {
            return ResponseEntity.badRequest().body("Nenhuma sessão ativa encontrada.");
        }
    }

    @PutMapping("/atualizar")
    public ResponseEntity<String> atualizarCliente(@RequestBody Cliente cliente, HttpSession session) {
        try {
            Cliente logado = (Cliente) session.getAttribute("clienteLogado");
            if (logado == null || !logado.getId().equals(cliente.getId())) {
                return ResponseEntity.status(403).body("Acesso negado.");
            }

            clienteService.atualizarCliente(cliente);
            logado.setNome(cliente.getNome());
            logado.setGenero(cliente.getGenero());
            session.setAttribute("clienteLogado", logado);

            return ResponseEntity.ok("Dados atualizados com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar: " + e.getMessage());
        }
    }

    @PostMapping("/endereco")
    public ResponseEntity<String> adicionarEndereco(@RequestBody EnderecoEntrega endereco, HttpSession session) {
        try {
            Cliente logado = (Cliente) session.getAttribute("clienteLogado");
            if (logado == null) {
                return ResponseEntity.status(403).body("Acesso negado.");
            }

            clienteService.adicionarEnderecoEntrega(logado.getId(), endereco);

            // Atualiza cliente e session com o novo endereço
            Cliente atualizado = clienteService.buscarPorEmail(logado.getEmail()).orElse(logado);
            session.setAttribute("clienteLogado", atualizado);

            return ResponseEntity.ok("Endereço adicionado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao adicionar endereço: " + e.getMessage());
        }
    }

    @PutMapping("/endereco/{id}/definir-padrao")
    public ResponseEntity<String> definirEnderecoPadrao(@PathVariable Long id, HttpSession session) {
        Cliente logado = (Cliente) session.getAttribute("clienteLogado");
        if (logado == null) {
            return ResponseEntity.status(403).body("Cliente não está logado.");
        }

        try {
            clienteService.definirEnderecoPadrao(logado.getId(), id);
            Cliente atualizado = clienteService.buscarPorEmail(logado.getEmail()).orElse(logado);
            session.setAttribute("clienteLogado", atualizado);
            return ResponseEntity.ok("Endereço padrão atualizado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

}
