/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.masterspi.service;

import com.masterspi.model.Cliente;
import com.masterspi.model.ItemPedido;
import com.masterspi.model.Pedido;
import com.masterspi.model.Produto;
import com.masterspi.repository.ClienteRepository;
import com.masterspi.repository.PedidoRepository;
import com.masterspi.repository.ProdutoRepository;
import dto.ItemPedidoDTO;
import dto.PedidoDTO;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Thiago
 */
@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public PedidoDTO criarPedido(PedidoDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setEnderecoEntrega(dto.getEnderecoEntrega());
        pedido.setFormaPagamento(dto.getFormaPagamento());
        pedido.setValorTotal(dto.getValorTotal());
        pedido.setStatus("aguardando pagamento");
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setValorFrete(dto.getValorFrete());

        Integer ultimoNumero = pedidoRepository.buscarUltimoNumeroPedido();
        pedido.setNumeroPedido((ultimoNumero != null ? ultimoNumero : 0) + 1);

        List<ItemPedido> itens = dto.getItens().stream().map(itemDTO -> {
            ItemPedido item = new ItemPedido();
            item.setProdutoId(itemDTO.getProdutoId());
            item.setNome(itemDTO.getNome());
            item.setPreco(itemDTO.getPreco());
            item.setQuantidade(itemDTO.getQuantidade());
            item.setSubtotal(itemDTO.getPreco() * itemDTO.getQuantidade());
            item.setPedido(pedido);
            return item;
        }).collect(Collectors.toList());

        pedido.setItens(itens);

        Pedido salvo = pedidoRepository.save(pedido);

        PedidoDTO retorno = new PedidoDTO();
        retorno.setNumeroPedido(salvo.getNumeroPedido());
        retorno.setValorTotal(salvo.getValorTotal());
        return retorno;
    }

    public List<PedidoDTO> buscarPedidosPorCliente(Long clienteId) {
        List<Pedido> pedidos = pedidoRepository.findByClienteIdOrderByDataCriacaoDesc(clienteId);
        return pedidos.stream().map(pedido -> {
            PedidoDTO dto = new PedidoDTO();
            dto.setId(pedido.getId());
            dto.setNumeroPedido(pedido.getNumeroPedido());
            dto.setValorTotal(pedido.getValorTotal());
            dto.setStatus(pedido.getStatus());
            dto.setDataCriacao(pedido.getDataCriacao());
            return dto;
        }).collect(Collectors.toList());
    }

    public PedidoDTO buscarPedidoPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId());
        dto.setNumeroPedido(pedido.getNumeroPedido());
        dto.setValorTotal(pedido.getValorTotal());
        dto.setStatus(pedido.getStatus());
        dto.setDataCriacao(pedido.getDataCriacao());
        dto.setEnderecoEntrega(pedido.getEnderecoEntrega());
        dto.setFormaPagamento(pedido.getFormaPagamento());
        dto.setValorFrete(pedido.getValorFrete());

        List<ItemPedidoDTO> itens = pedido.getItens().stream().map(item -> {
            ItemPedidoDTO i = new ItemPedidoDTO();

            // busca dados completos do produto
            Produto produto = produtoRepository.findById(item.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            i.setProdutoId(item.getProdutoId());
            i.setNome(produto.getNome());
            i.setPreco(item.getPreco());
            i.setQuantidade(item.getQuantidade());

            // obtém URL da imagem principal (ou fallback)
            String url = produto.getImagemPrincipal();
            if (url == null || url.isEmpty()) {
                List<String> imgs = produto.getImagens();
                url = (imgs != null && !imgs.isEmpty())
                        ? imgs.get(0)
                        : "assets/imagens/placeholder.png";
            }
            i.setImagemUrl(url);

            return i;
        }).collect(Collectors.toList());

        dto.setItens(itens);
        return dto;
    }

    public List<PedidoDTO> buscarTodosPedidos() {
        return pedidoRepository
                .findAllByOrderByDataCriacaoDesc()
                .stream()
                .map(p -> {
                    PedidoDTO dto = new PedidoDTO();
                    dto.setId(p.getId());
                    dto.setNumeroPedido(p.getNumeroPedido());
                    dto.setDataCriacao(p.getDataCriacao());
                    dto.setValorTotal(p.getValorTotal());
                    dto.setStatus(p.getStatus());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void atualizarStatus(Long pedidoId, String novoStatus) {
        Pedido p = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        p.setStatus(novoStatus);
        pedidoRepository.save(p);
    }

}
