package com.masterspi.model;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
/**
 *
 * @author Thiago
 */
public enum Status {
    AGUARDANDO_PAGAMENTO,
    PAGAMENTO_REJEITADO,
    PAGAMENTO_COM_SUCESSO,
    AGUARDANDO_RETIRADA,
    EM_TRANSITO,
    ENTREGUE;

    @Override
    public String toString() {
        return name()
            .toLowerCase()
            .replace('_', ' ');
    }
}
