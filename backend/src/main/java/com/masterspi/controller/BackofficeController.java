package com.masterspi.controller;

import com.masterspi.model.Produto;
import com.masterspi.model.User;
import com.masterspi.service.AccessControlService;
import com.masterspi.service.ProdutoService;
import com.masterspi.service.UserService;
import java.math.BigDecimal;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class BackofficeController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccessControlService accessControlService;

    @Autowired
    private ProdutoService produtoService;

    // Rota Login
    @GetMapping("/backoffice/login")
    public String showBackofficeLogin() {
        return "login";
    }

    // Rota Backoffice
    @GetMapping("/backoffice")
    public String showBackofficeHome(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));
        boolean isEstoquista = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ESTOQUISTA"));
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isEstoquista", isEstoquista);
        return "backoffice";
    }

    // Rota Erro 403
    @GetMapping("/403")
    public String acessoNegado() {
        return "403";
    }

    // Listagem de usuários
    @GetMapping("/backoffice/usuarios")
    public String listarUsuarios(@RequestParam(value = "nome", required = false) String nome, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User usuarioLogado = userService.buscarPorEmail(auth.getName()).orElseThrow();

        List<User> usuarios = userService.listarUsuarios(nome);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("loggedInUser", usuarioLogado.getEmail());
        return "usuarios";
    }

    // Listagem de produtos
    @GetMapping("/backoffice/produtos")
    public String listarProdutos(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "pagina", defaultValue = "1") int pagina,
            Model model
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Já existente:
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
        // Adicione a verificação de Estoquista
        boolean isEstoquista = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ESTOQUISTA"));

        Page<Produto> paginaProdutos = produtoService.listarProdutos(nome, pagina);

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isEstoquista", isEstoquista);
        model.addAttribute("produtos", paginaProdutos.getContent());
        model.addAttribute("paginaAtual", pagina);
        model.addAttribute("totalPaginas", paginaProdutos.getTotalPages());
        model.addAttribute("nomeBusca", nome); // para manter o termo de busca

        return "produtos"; // seu template
    }

    // Rota Cadastro
    @GetMapping("/backoffice/usuarios/incluir")
    public String incluirUsuario(Model model) {
        model.addAttribute("usuario", new User());
        return "usuario-form";
    }

    // Rota para cadastro de produto 
    @GetMapping("/backoffice/produtos/incluir")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("produto", new Produto());
        return "produto-form";
    }

    // Rota Alterar usuario
    @GetMapping("/backoffice/usuarios/alterar/{id}")
    public String alterarUsuario(@PathVariable Long id, Model model) {
        User usuario = userService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = (auth != null) ? auth.getName() : "";

        model.addAttribute("usuario", usuario);
        model.addAttribute("loggedInUser", loggedInUser);
        return "usuario-form";
    }

    // Rota Alterar produto
    @GetMapping("/backoffice/produtos/alterar/{codigo}")
    public String alterarProduto(@PathVariable Long codigo, Model model) {
        Produto produto = produtoService.buscarPorCodigo(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        model.addAttribute("produto", produto);
        return "produto-form"; // Reutilizando a tela de cadastro
    }

    // Rota para ativar/inativar usuário
    @PostMapping("/backoffice/usuarios/toggle/{id}")
    public String toggleUsuario(@PathVariable Long id) {
        userService.alterarStatusUsuario(id);
        return "redirect:/backoffice/usuarios";
    }

    // Rota para ativar/inativar produto
    @PostMapping("/backoffice/produtos/toggle/{codigo}")
    public String toggleProduto(@PathVariable Long codigo) {
        produtoService.alterarStatusProduto(codigo);
        return "redirect:/backoffice/produtos";
    }

    // Rota para salvar (incluir ou alterar) usuário
    @PostMapping("/backoffice/usuarios/salvar")
    public String salvarUsuario(@ModelAttribute User usuario,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String confirmPassword,
            Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String loggedInUserEmail = (auth != null) ? auth.getName() : "";

            userService.salvarUsuario(usuario, password, confirmPassword, loggedInUserEmail);

            return "redirect:/backoffice/usuarios";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("usuario", usuario);
            return "usuario-form";
        }
    }

    // Rota para salvar produto
    @PostMapping("/backoffice/produtos/salvar")
    public String salvarProduto(
            @RequestParam(required = false) Long codigo, // Agora usa código
            @RequestParam String nome,
            @RequestParam double avaliacao,
            @RequestParam String descricao,
            @RequestParam BigDecimal valor,
            @RequestParam int estoque,
            @RequestParam("imagens") List<MultipartFile> imagens,
            @RequestParam(required = false) String imagemPrincipal) {

        boolean statusAtivo = true;

        // Conversão explícita da lista para array
        MultipartFile[] imagensArray = imagens.toArray(new MultipartFile[0]);

        if (codigo == null) {
            // Cadastro de novo produto
            produtoService.salvarProduto(nome, avaliacao, descricao, valor, estoque, statusAtivo, imagensArray, imagemPrincipal);
        } else {
            // Edição de produto existente
            produtoService.alterarProduto(codigo, nome, avaliacao, descricao, valor, estoque, Arrays.asList(imagensArray), imagemPrincipal);
        }

        return "redirect:/backoffice/produtos";
    }

    @GetMapping("/backoffice/produtos/estoquista-alterar/{codigo}")
    public String estoquistaAlterarEstoque(@PathVariable Long codigo, Model model) {
        Produto produto = produtoService.buscarPorCodigo(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        // Carrega o produto e envia para um form simples
        model.addAttribute("produto", produto);
        return "estoque-form";
    }

    @PostMapping("/backoffice/produtos/estoquista-salvar")
    public String estoquistaSalvarEstoque(
            @RequestParam Long codigo,
            @RequestParam int estoque
    ) {
        // Busca o produto e altera só o estoque
        Produto produto = produtoService.buscarPorCodigo(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        produto.setEstoque(estoque);
        produtoService.save(produto); // ou repository.save

        return "redirect:/backoffice/produtos";
    }

    @GetMapping("/backoffice/produtos/visualizar/{codigo}")
    public String visualizarProduto(@PathVariable Long codigo, Model model) {
        Produto produto = produtoService.buscarPorCodigo(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        model.addAttribute("produto", produto);
        return "produto-detalhe"; // Retorna a página criada
    }

}
