package com.masterspi.controller;

import com.masterspi.model.User;
import com.masterspi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BackofficeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    // Rota para ativar/inativar usuário (toggle)
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

        if (usuario.getId() != null) { // Edição de usuário
            User usuarioExistente = userRepository.findById(usuario.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
            // Mantém o email inalterado
            usuario.setEmail(usuarioExistente.getEmail());

            // Se o usuário logado for o mesmo que está sendo alterado, não permite alterar o grupo
            if (usuario.getEmail().equals(loggedInUserEmail)) {
                usuario.setRole(usuarioExistente.getRole());
            }

            // Se a senha estiver preenchida (já criptografada pelo front-end), utiliza-a; caso contrário, mantém a senha atual
            if (password != null && !password.isEmpty()) {
                if (!password.equals(confirmPassword)) {
                    model.addAttribute("error", "As senhas não coincidem!");
                    return "usuario-form";
                }
                usuario.setPassword(password); // Já está criptografada no cliente
            } else {
                usuario.setPassword(usuarioExistente.getPassword());
            }
        } else { // Inclusão de novo usuário
            if (password == null || password.isEmpty() || !password.equals(confirmPassword)) {
                model.addAttribute("error", "As senhas não coincidem ou não foram preenchidas!");
                return "usuario-form";
            }
            usuario.setPassword(password); // Valor já criptografado pelo cliente
        }

        userRepository.save(usuario);
        return "redirect:/backoffice/usuarios";
    }
}
