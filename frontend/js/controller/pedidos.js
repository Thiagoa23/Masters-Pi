// js/controller/pedidos.js

document.addEventListener("DOMContentLoaded", async () => {
  const cliente = JSON.parse(sessionStorage.getItem("clienteLogado"));
  const container = document.getElementById("lista-pedidos");
  if (!cliente?.id) {
    container.innerHTML = "<p>Você precisa estar logado para ver seus pedidos.</p>";
    return;
  }

  // 1) Carrega pedidos resumidos
  let pedidos = [];
  try {
    const resp = await fetch(`http://localhost:8080/api/pedido/cliente/${cliente.id}`);
    if (!resp.ok) throw new Error("HTTP " + resp.status);
    pedidos = await resp.json();
  } catch {
    container.innerHTML = "<p>Erro ao buscar pedidos.</p>";
    return;
  }
  if (!pedidos.length) {
    container.innerHTML = "<p>Você ainda não tem pedidos.</p>";
    return;
  }

  // 2) Renderiza cards de resumo
  container.innerHTML = pedidos.map(p => {
    let dataFormatada = "—";
    if (Array.isArray(p.dataCriacao)) {
      const [y, m, d, h, min] = p.dataCriacao;
      dataFormatada =
        `${String(d).padStart(2, "0")}/${String(m).padStart(2, "0")}/${y} ` +
        `${String(h).padStart(2, "0")}:${String(min).padStart(2, "0")}`;
    }
    return `
      <div class="pedido-card" data-id="${p.id}">
        <p><strong>Nº do Pedido:</strong> ${p.numeroPedido}</p>
        <p><strong>Data:</strong> ${dataFormatada}</p>
        <p><strong>Status:</strong> ${p.status}</p>
        <p><strong>Valor Total:</strong> R$ ${p.valorTotal.toFixed(2)}</p>
        <button class="btn-detalhes" data-open="false">Mais detalhes</button>
      </div>
    `;
  }).join("");

  // 3) Toggle detalhes, agora apontando para o backend uploads (porta 8080)
  container.querySelectorAll(".pedido-card").forEach(card => {
    const btn = card.querySelector(".btn-detalhes");
    btn.addEventListener("click", () => {
      const id = card.dataset.id;
      // sobe ao checkout e abre a tela de pedido-detalhe.html
      window.location.href = `pedido-detalhe.html?id=${id}`;
    });
  });
});
