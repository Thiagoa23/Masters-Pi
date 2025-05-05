/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import com.masterspi.model.EnderecoEntrega;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Thiago
 */
@Data
public class PedidoDTO {

    private Long id;
    private Long clienteId;
    private EnderecoEntrega enderecoEntrega;
    private String formaPagamento;
    private Double valorTotal;
    private List<ItemPedidoDTO> itens;
    private String status;
    private LocalDateTime dataCriacao;

    private Integer numeroPedido;
    private Double valor;
}
