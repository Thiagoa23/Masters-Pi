package com.masterspi.controller;

import com.masterspi.model.User;
import com.masterspi.service.AccessControlService;
import com.masterspi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class BackofficeController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccessControlService accessControlService;

    // Rota Login
    @GetMapping("/backoffice/login")
    public String showBackofficeLogin() {
        return "login";
    }

    // Rota Backoffice
    @GetMapping("/backoffice")
    public String showBackofficeHome(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        return "backoffice";
    }

    // Rota Erro 403
    @GetMapping("/403")
    public String acessoNegado() {
        return "403";
    }

    // Listagem de usuários
    @GetMapping("/backoffice/usuarios")
    public String listarUsuarios(@RequestParam(value = "nome", required = false) String nome, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User usuarioLogado = userService.buscarPorEmail(auth.getName()).orElseThrow();

        List<User> usuarios = userService.listarUsuarios(nome);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("loggedInUser", usuarioLogado.getEmail());
        return "usuarios";
    }

    // Rota Cadastro
    @GetMapping("/backoffice/usuarios/incluir")
    public String incluirUsuario(Model model) {
        model.addAttribute("usuario", new User());
        return "usuario-form";
    }

    // Rota Alterar
    @GetMapping("/backoffice/usuarios/alterar/{id}")
    public String alterarUsuario(@PathVariable Long id, Model model) {
        User usuario = userService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = (auth != null) ? auth.getName() : "";

        model.addAttribute("usuario", usuario);
        model.addAttribute("loggedInUser", loggedInUser);
        return "usuario-form";
    }

    // Rota para ativar/inativar usuário
    @PostMapping("/backoffice/usuarios/toggle/{id}")
    public String toggleUsuario(@PathVariable Long id) {
        userService.alterarStatusUsuario(id);
        return "redirect:/backoffice/usuarios";
    }

    // Rota para salvar (incluir ou alterar) usuário
    @PostMapping("/backoffice/usuarios/salvar")
    public String salvarUsuario(@ModelAttribute User usuario,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String confirmPassword,
            Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String loggedInUserEmail = (auth != null) ? auth.getName() : "";

            userService.salvarUsuario(usuario, password, confirmPassword, loggedInUserEmail);

            return "redirect:/backoffice/usuarios";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("usuario", usuario);
            return "usuario-form";
        }
    }

}
