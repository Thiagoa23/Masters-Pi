package com.masterspi.service;

import com.masterspi.model.Cliente;
import com.masterspi.model.EnderecoEntrega;
import com.masterspi.model.EnderecoFaturamento;
import com.masterspi.repository.ClienteRepository;
import com.masterspi.util.CPFValidator;
import java.util.List;
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

        if (cliente.getFaturamento() == null) {
            throw new Exception("Endereço de faturamento é obrigatório.");
        }

        if (cliente.getEnderecosEntrega() == null || cliente.getEnderecosEntrega().isEmpty()) {
            throw new Exception("Pelo menos um endereço de entrega deve ser informado.");
        }

        // 🔒 Criptografar senha antes de salvar
        String senhaCriptografada = passwordEncoder.encode(cliente.getSenha());
        cliente.setSenha(senhaCriptografada);

        // 🧹 Salva cópias dos endereços antes de limpar
        EnderecoFaturamento faturamentoTemp = cliente.getFaturamento();
        List<EnderecoEntrega> entregasTemp = cliente.getEnderecosEntrega();

        // ⚡ Primeiro salva o cliente sem endereço
        cliente.setFaturamento(null);
        cliente.setEnderecosEntrega(null);
        Cliente clienteSalvo = clienteRepository.save(cliente);

        // 🔗 Agora associa os endereços ao cliente salvo
        faturamentoTemp.setCliente(clienteSalvo);

        boolean marcouPadrao = false;
        for (EnderecoEntrega entrega : entregasTemp) {
            entrega.setCliente(clienteSalvo);
            if (!marcouPadrao) {
                entrega.setPadrao(true);
                marcouPadrao = true;
            } else {
                entrega.setPadrao(false);
            }
        }

        // 🔥 Atualiza cliente já salvo com endereços vinculados
        clienteSalvo.setFaturamento(faturamentoTemp);
        clienteSalvo.setEnderecosEntrega(entregasTemp);

        clienteRepository.save(clienteSalvo);
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
        if (nome == null) {
            return false;
        }
        String[] partes = nome.trim().split("\\s+");
        if (partes.length < 2) {
            return false;
        }

        for (String parte : partes) {
            if (parte.length() < 3) {
                return false;
            }
        }
        return true;
    }

    public void atualizarCliente(Cliente clienteAtualizado) throws Exception {
        Cliente clienteExistente = clienteRepository.findById(clienteAtualizado.getId())
                .orElseThrow(() -> new Exception("Cliente não encontrado."));

        if (!nomeValido(clienteAtualizado.getNome())) {
            throw new Exception("Nome inválido. Deve conter ao menos duas palavras com 3 letras ou mais.");
        }

        clienteExistente.setNome(clienteAtualizado.getNome());
        clienteExistente.setGenero(clienteAtualizado.getGenero());
        clienteExistente.setDataNascimento(clienteAtualizado.getDataNascimento());

        if (clienteAtualizado.getSenha() != null && !clienteAtualizado.getSenha().isBlank()) {
            String senhaCriptografada = passwordEncoder.encode(clienteAtualizado.getSenha());
            clienteExistente.setSenha(senhaCriptografada);
        }

        clienteRepository.save(clienteExistente);
    }

    public void adicionarEnderecoEntrega(Long clienteId, EnderecoEntrega novoEndereco) throws Exception {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new Exception("Cliente não encontrado."));

        // 🔧 Força o Hibernate a carregar a coleção lazy antes de manipular
        cliente.getEnderecosEntrega().size();

        novoEndereco.setCliente(cliente);

        boolean definirComoPadrao = cliente.getEnderecosEntrega().isEmpty();
        novoEndereco.setPadrao(definirComoPadrao);

        cliente.getEnderecosEntrega().add(novoEndereco);
        clienteRepository.save(cliente);
    }

    public void definirEnderecoPadrao(Long clienteId, Long idEnderecoPadrao) throws Exception {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new Exception("Cliente não encontrado."));

        cliente.getEnderecosEntrega().size(); // força carregamento

        boolean encontrou = false;
        for (EnderecoEntrega endereco : cliente.getEnderecosEntrega()) {
            if (endereco.getId().equals(idEnderecoPadrao)) {
                endereco.setPadrao(true);
                encontrou = true;
            } else {
                endereco.setPadrao(false);
            }
        }

        if (!encontrou) {
            throw new Exception("Endereço informado não pertence ao cliente.");
        }

        clienteRepository.save(cliente);
    }

}
