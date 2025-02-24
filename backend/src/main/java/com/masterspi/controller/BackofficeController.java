package com.masterspi.controller;

import com.masterspi.model.User;
import com.masterspi.repository.UserRepository;
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
    private UserRepository userRepository;

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
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        return "backoffice";
    }

    //Rota Erro 403
    @GetMapping("/403")
    public String acessoNegado() {
        return "403";
    }

    // Listagem e Filtro
    @GetMapping("/backoffice/usuarios")
    public String listarUsuarios(@RequestParam(value = "nome", required = false) String nome, Model model) {
        List<User> usuarios = (nome != null && !nome.isEmpty())
                ? userRepository.findByNomeContainingIgnoreCase(nome)
                : userRepository.findAll();

        // salva usuario logado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = (auth != null) ? auth.getName() : "";

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("loggedInUser", loggedInUser);
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
        User usuario = userRepository.findById(id)
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
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        usuario.setEnabled(!usuario.isEnabled());
        userRepository.save(usuario);
        return "redirect:/backoffice/usuarios";
    }

    // Rota para salvar (incluir ou alterar) usuário
    @PostMapping("/backoffice/usuarios/salvar")
    public String salvarUsuario(@ModelAttribute User usuario,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String confirmPassword,
            Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserEmail = (auth != null) ? auth.getName() : "";

        // Validação do CPF
        if (!com.masterspi.util.CPFValidator.isValidCPF(usuario.getCpf())) {
            model.addAttribute("error", "CPF inválido!");
            model.addAttribute("usuario", usuario);
            return "usuario-form";
        }

        if (usuario.getId() != null) { //Alterar
            User usuarioExistente = userRepository.findById(usuario.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
            // Mantém o email inalterado
            usuario.setEmail(usuarioExistente.getEmail());

            //mantem o grupo
            if (usuarioExistente.getEmail().equals(loggedInUserEmail)) {
                usuario.setRole(usuarioExistente.getRole());
            }

            // usuário logado não pode alterar seu proprio grupo
            if (usuario.getEmail().equals(loggedInUserEmail)) {
                usuario.setRole(usuarioExistente.getRole());
            }

            // Validação de Senha
            if (password != null && !password.isEmpty()) {
                if (!password.equals(confirmPassword)) {
                    model.addAttribute("error", "As senhas não coincidem!");
                    return "usuario-form";
                }
                usuario.setPassword(password);
            } else {
                usuario.setPassword(usuarioExistente.getPassword());
            }
        } else { // Incluir

            //verificação email
            if (userRepository.findByEmail(usuario.getEmail()).isPresent()) {
                model.addAttribute("error", "Já existe um usuário cadastrado com este email!");
                model.addAttribute("usuario", usuario);
                return "usuario-form";
            }

            //validação senha
            if (password == null || password.isEmpty() || !password.equals(confirmPassword)) {
                model.addAttribute("error", "As senhas não coincidem ou não foram preenchidas!");
                model.addAttribute("usuario", usuario);
                return "usuario-form";
            }
            usuario.setPassword(password);
            usuario.setEnabled(true);
        }

        userRepository.save(usuario);
        return "redirect:/backoffice/usuarios";
    }
}
