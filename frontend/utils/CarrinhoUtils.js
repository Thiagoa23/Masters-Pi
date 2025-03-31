export function atualizarContadorCarrinho() {
    const contador = document.getElementById("contador-carrinho");
    if (!contador) return;
  
    const carrinho = JSON.parse(sessionStorage.getItem("carrinho")) || [];
    const total = carrinho.reduce((soma, item) => soma + item.quantidade, 0);
    contador.textContent = total;
  }
  