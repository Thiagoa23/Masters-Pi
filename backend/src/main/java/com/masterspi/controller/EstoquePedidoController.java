package com.masterspi.controller;

import com.masterspi.service.PedidoService;
import dto.PedidoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/estoque")
public class EstoquePedidoController {

    private final PedidoService pedidoService;

    @Autowired
    public EstoquePedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/pedidos")
    public String listarPedidosEstoque(Model model) {
        model.addAttribute("pedidos",
                pedidoService.buscarTodosPedidos());
        return "pedidos-estoque";
    }

    @GetMapping("/pedido/{id}")
    public String editarStatus(@PathVariable Long id, Model model) {
        PedidoDTO dto = pedidoService.buscarPedidoPorId(id);
        model.addAttribute("pedido", dto);
        return "editar-status";
    }

    @PostMapping("/pedido/{id}/status")
    public String salvarStatus(@PathVariable Long id,
            @ModelAttribute("pedido") PedidoDTO pedidoDto) {
        pedidoService.atualizarStatus(id, pedidoDto.getStatus());
        return "redirect:/estoque/pedidos";
    }
}
