package com.masterspi.service;

import com.masterspi.model.Cliente;
import com.masterspi.model.EnderecoEntrega;
import com.masterspi.repository.ClienteRepository;
import com.masterspi.util.CPFValidator;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void cadastrarCliente(Cliente cliente) throws Exception {
        if (!nomeValido(cliente.getNome())) {
            throw new Exception("Nome inválido. Deve conter ao menos duas palavras com 3 letras ou mais.");
        }

        if (!CPFValidator.isValidCPF(cliente.getCpf())) {
            throw new Exception("CPF inválido.");
        }
        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new Exception("CPF já cadastrado.");
        }

        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new Exception("E-mail já cadastrado.");
        }

        if (cliente.getEnderecosEntrega() == null || cliente.getEnderecosEntrega().isEmpty()) {
            throw new Exception("Pelo menos um endereço de entrega deve ser informado.");
        }

        boolean marcouPadrao = false;
        for (EnderecoEntrega endereco : cliente.getEnderecosEntrega()) {
            endereco.setCliente(cliente);
            if (!marcouPadrao) {
                endereco.setPadrao(true);
                marcouPadrao = true;
            } else {
                endereco.setPadrao(false);
            }
        }

        String senhaCriptografada = passwordEncoder.encode(cliente.getSenha());
        cliente.setSenha(senhaCriptografada);

        clienteRepository.save(cliente);
    }

    public Cliente loginCliente(String email, String senha) throws Exception {
        System.out.println("Tentando login com email: " + email + " e senha: " + senha);
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("E-mail não encontrado."));

        if (!passwordEncoder.matches(senha, cliente.getSenha())) {
            throw new Exception("Senha incorreta.");
        }

        return cliente;
    }

    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    private boolean nomeValido(String nome) {
        if (nome == null) return false;
        String[] partes = nome.trim().split("\\s+");
        if (partes.length < 2) return false;

        for (String parte : partes) {
            if (parte.length() < 3) return false;
        }
        return true;
    }
}
