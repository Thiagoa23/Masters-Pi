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
        `${String(d).padStart(2,"0")}/${String(m).padStart(2,"0")}/${y} ` +
        `${String(h).padStart(2,"0")}:${String(min).padStart(2,"0")}`;
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
    btn.addEventListener("click", async () => {
      const aberto = btn.dataset.open === "true";

      if (!aberto) {
        const resp = await fetch(`http://localhost:8080/api/pedido/${card.dataset.id}`);
        const full = await resp.json();

        const itemsHtml = (full.itens || []).map(i => {
          // usa a porta 8080 para buscar uploads do backend
          const src = i.imagemUrl
            ? `http://localhost:8080/uploads/${i.imagemUrl}`
            : `http://localhost:8080/uploads/placeholder.png`;

          return `
            <div class="item-card">
              <img src="${src}" alt="${i.nome}" class="item-img" />
              <div class="item-info">
                <h4>${i.nome}</h4>
                <p><strong>Preço:</strong> R$ ${i.preco.toFixed(2)}</p>
                <p><strong>Quantidade:</strong> ${i.quantidade}</p>
                <p><strong>Subtotal:</strong> R$ ${(i.preco * i.quantidade).toFixed(2)}</p>
              </div>
            </div>
          `;
        }).join("");

        card.insertAdjacentHTML("beforeend", `
          <div class="pedido-items">
            ${itemsHtml}
          </div>
        `);

        btn.textContent = "Menos detalhes";
        btn.dataset.open = "true";

      } else {
        const detalhes = card.querySelector(".pedido-items");
        if (detalhes) detalhes.remove();
        btn.textContent = "Mais detalhes";
        btn.dataset.open = "false";
      }
    });
  });
});
