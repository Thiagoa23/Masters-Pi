/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import lombok.Data;

/**
 *
 * @author Thiago
 */
@Data
public class ItemPedidoDTO {
    private Long produtoId;
    private String nome;
    private Double preco;
    private Integer quantidade;
    private String imagemUrl;
}
