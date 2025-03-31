import { ProdutoDetalheView } from "../view/ProdutoDetalheView.js";
import { atualizarContadorCarrinho } from "../../utils/CarrinhoUtils.js";

document.addEventListener("DOMContentLoaded", () => {
  const params = new URLSearchParams(window.location.search);
  const codigo = params.get("codigo");

  if (!codigo) {
    alert("Produto não encontrado.");
    return;
  }

  fetch(`http://localhost:8080/api/produtos/${codigo}`)
    .then(response => {
      if (!response.ok) throw new Error("Produto não encontrado.");
      return response.json();
    })
    .then(produto => {
      ProdutoDetalheView.renderizar(produto);

      const btnAdicionar = document.getElementById("btn-adicionar-carrinho");
      const btnComprar = document.getElementById("btn-comprar");

      const adicionarAoCarrinho = () => {
        const carrinho = JSON.parse(sessionStorage.getItem("carrinho")) || [];
        const itemExistente = carrinho.find(p => p.codigo === produto.codigo);

        if (itemExistente) {
          itemExistente.quantidade += 1;
        } else {
          carrinho.push({
            codigo: produto.codigo,
            nome: produto.nome,
            valor: produto.valor,
            quantidade: 1,
            imagem: produto.imagemPrincipal
          });
        }

        sessionStorage.setItem("carrinho", JSON.stringify(carrinho));
        atualizarContadorCarrinho();
      };

      if (btnAdicionar) {
        btnAdicionar.addEventListener("click", () => {
          adicionarAoCarrinho();

          const irParaCarrinho = confirm("Produto adicionado ao carrinho! Deseja ir para o carrinho agora?");
          if (irParaCarrinho) {
            window.location.href = "carrinho.html";
          } else {
            window.location.href = "index.html";
          }
        });
      }

      if (btnComprar) {
        btnComprar.addEventListener("click", () => {
          adicionarAoCarrinho();
          window.location.href = "carrinho.html";
        });
      }
    })
    .catch(error => {
      console.error("Erro ao buscar produto:", error);
      alert("Não foi possível carregar os dados do produto.");
    });
});
