package com.masterspi.controller;

import com.masterspi.model.User;
import com.masterspi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Controller
public class BackofficeController {

    @Autowired
    private UserRepository userRepository;

    // Página de Login
    @GetMapping("/backoffice/login")
    public String showBackofficeLogin() {
        return "login";
    }

    // Página Principal do Backoffice
    @GetMapping("/backoffice")
    public String showBackofficeHome(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        return "backoffice";
    }

    // Listagem e Filtro de Usuários
    @GetMapping("/backoffice/usuarios")
    public String listarUsuarios(@RequestParam(value = "nome", required = false) String nome, Model model) {
        List<User> usuarios = (nome != null && !nome.isEmpty())
                ? userRepository.findByNomeContainingIgnoreCase(nome)
                : userRepository.findAll();

        // Captura o usuário autenticado e passa para o modelo
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = (auth != null) ? auth.getName() : "";

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("loggedInUser", loggedInUser);
        return "usuarios";
    }

    // Rota para incluir usuário (apenas exibe a página de cadastro)
    @GetMapping("/backoffice/usuarios/incluir")
    public String incluirUsuario(Model model) {
        model.addAttribute("usuario", new User());
        return "usuario-form";
    }

    // Rota para alterar usuário (apenas exibe a página de alteração)
    @GetMapping("/backoffice/usuarios/alterar/{id}")
    public String alterarUsuario(@PathVariable Long id, Model model) {
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        model.addAttribute("usuario", usuario);
        return "usuario-form";
    }
}
