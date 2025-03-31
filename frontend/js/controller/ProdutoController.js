import { ProdutoView } from '../view/ProdutoView.js';

fetch("http://localhost:8080/api/produtos/ativos")
  .then(resposta => {
    if (!resposta.ok) {
      throw new Error("Erro ao buscar produtos");
    }
    return resposta.json();
  })
  .then(produtos => {
    produtos.forEach(produto => {
      ProdutoView.renderizar(produto);
    });
  })
  .catch(erro => {
    console.error("Erro ao carregar produtos:", erro);
  });
