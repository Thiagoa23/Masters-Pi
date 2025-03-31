export const ProdutoView = {
  renderizar(produto) {
    const container = document.getElementById("cards");

    const card = document.createElement("div");
    card.className = "card";

    card.innerHTML = `
      <img src="http://localhost:8080/uploads/${produto.imagemPrincipal}" alt="${produto.nome}">
      <h3>${produto.nome}</h3>
      <p>R$ ${produto.valor.toFixed(2)}</p>
      <a href="produto-detalhe.html?codigo=${produto.codigo}" class="btn">Detalhes</a>
      <button class="btn-comprar">Comprar</button>
    `;

    const botaoComprar = card.querySelector(".btn-comprar");
    botaoComprar.addEventListener("click", () => {
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
      window.location.href = "carrinho.html";
    });

    container.appendChild(card);
  }
};
