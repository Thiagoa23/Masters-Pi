package com.masterspi.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(nullable = false)
    private int estoque;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false)
    private boolean ativo;

    @Column(nullable = false, length = 2000)
    private String descricao;

    @Column(nullable = false)
    private double avaliacao;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "produto_imagens", joinColumns = @JoinColumn(name = "produto_id"))
    @Column(name = "caminho_imagem")
    private List<String> imagens;

    @Column(name = "imagem_principal")
    private String imagemPrincipal;  // caminho relativo da imagem principal
}
