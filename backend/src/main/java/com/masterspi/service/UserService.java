package com.masterspi.service;

import com.masterspi.model.User;
import com.masterspi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserValidationService userValidationService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserValidationService userValidationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userValidationService = userValidationService;
    }

    public List<User> listarUsuarios(String nome) {
        return (nome != null && !nome.isEmpty())
                ? userRepository.findByNomeContainingIgnoreCase(nome)
                : userRepository.findAll();
    }

    public Optional<User> buscarPorEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> buscarPorId(Long id) {
        return userRepository.findById(id);
    }

    public void salvarUsuario(User usuario, String senha, String confirmPassword, String loggedInUserEmail) {
        userValidationService.validarDadosUsuario(usuario, senha, confirmPassword, loggedInUserEmail);

        if (usuario.getId() == null) { 
            usuario.setEnabled(true);
        } else { 
            User usuarioExistente = userRepository.findById(usuario.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

            usuario.setEmail(usuarioExistente.getEmail()); 
            usuario.setEnabled(usuarioExistente.isEnabled()); 
        }

        if (senha != null && !senha.isEmpty() && !senha.startsWith("$2a$")) {
            usuario.setPassword(passwordEncoder.encode(senha));
        }

        userRepository.save(usuario);
    }

    public void alterarStatusUsuario(Long id) {
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        usuario.setEnabled(!usuario.isEnabled());
        userRepository.save(usuario);
    }
}
