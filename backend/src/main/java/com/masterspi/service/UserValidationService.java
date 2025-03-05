package com.masterspi.service;

import com.masterspi.model.User;
import com.masterspi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserValidationService {

    private final UserRepository userRepository;
    private final CPFValidatorService cpfValidatorService;
    private final PasswordValidationService passwordValidationService;

    @Autowired
    public UserValidationService(UserRepository userRepository, CPFValidatorService cpfValidatorService, PasswordValidationService passwordValidationService) {
        this.userRepository = userRepository;
        this.cpfValidatorService = cpfValidatorService;
        this.passwordValidationService = passwordValidationService;
    }

    public void validarDadosUsuario(User usuario, String senha, String confirmPassword, String loggedInUserEmail) {
        if (usuario.getId() == null && userRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este email.");
        }

        if (!cpfValidatorService.isValid(usuario.getCpf())) {
            throw new IllegalArgumentException("CPF inválido.");
        }

        if (!passwordValidationService.isValid(senha, confirmPassword)) {
            throw new IllegalArgumentException("As senhas não coincidem!");
        }

        if (usuario.getId() != null) {
            User usuarioExistente = userRepository.findById(usuario.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

            if (usuario.getEmail().equals(loggedInUserEmail)) {
                usuario.setRole(usuarioExistente.getRole());
            }
        }
    }

}
