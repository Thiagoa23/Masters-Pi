// frontend/js/controller/PedidoDetalheController.js
document.addEventListener("DOMContentLoaded", async () => {
  const container = document.getElementById("detalhe-container");
  const params = new URLSearchParams(window.location.search);
  const id = params.get("id");
  if (!id) {
    container.innerHTML = "<p>ID do pedido não informado.</p>";
    return;
  }

  try {
    const res = await fetch(`http://localhost:8080/api/pedido/${id}`);
    if (!res.ok) throw new Error("HTTP " + res.status);
    const p = await res.json();

    // cabeçalho
    const data = Array.isArray(p.dataCriacao)
      ? new Date(p.dataCriacao[0], p.dataCriacao[1] - 1, p.dataCriacao[2], p.dataCriacao[3], p.dataCriacao[4])
      : new Date(p.dataCriacao);

    let html = `
      <p><strong>Nº do Pedido:</strong> ${p.numeroPedido}</p>
      <p><strong>Data:</strong> ${data.toLocaleString("pt-BR")}</p>
      <p><strong>Status:</strong> ${p.status}</p>
      <hr>`;

    // itens
    html += `<section><h2>Itens</h2><div class="itens-scroll">`;
    (p.itens || []).forEach(i => {
      const src = i.imagemUrl
        ? `http://localhost:8080/uploads/${i.imagemUrl}`
        : `http://localhost:8080/uploads/placeholder.png`;
      html += `
    <div class="item-card">
      <img src="${src}" alt="${i.nome}">
      <div class="item-info">
        <p>${i.nome}</p>
        <p>Qtd: ${i.quantidade}</p>
        <p>Subtotal: R$ ${(i.preco * i.quantidade).toFixed(2)}</p>
      </div>
    </div>`;
    });
    html += `</div></section>`;

    // totais
    const frete = Number(p.valorFrete ?? 0);
    const total = Number(p.valorTotal ?? 0);
    html += `
      <section class="totais">
        <p><strong>Frete:</strong> R$ ${frete.toFixed(2)}</p>
        <p><strong>Total:</strong> R$ ${total.toFixed(2)}</p>
      </section>`;

    // endereço
    if (p.enderecoEntrega) {
      const e = p.enderecoEntrega;
      html += `
     <section>
        <h2>Endereço de Entrega</h2>
        <p>${e.logradouro}, ${e.numero} – ${e.bairro}</p>
        <p>${e.cidade}/${e.uf} – CEP: ${e.cep}</p>
      </section>`;
    } else {
      html += `
      <section>
        <h2>Endereço de Entrega</h2>
        <p><em>Não há endereço de entrega cadastrado para este pedido.</em></p>
      </section>`;
    }

    // pagamento
    let formaTexto;
    if (p.formaPagamento === "boleto") {
      formaTexto = "Boleto Bancário";
    } else if (p.formaPagamento === "cartao") {
      formaTexto = "Cartão de Crédito";
    } else {
      formaTexto = p.formaPagamento;
    }
    html += ` 
    <section> 
      <h2>Forma de Pagamento</h2> 
      <p>${formaTexto}</p> 
    </section>`;

    // botão voltar
    html += `<a class="btn-voltar" href="pedidos.html">← Voltar aos pedidos</a>`;

    container.innerHTML = html;
  } catch (err) {
    console.error(err);
    container.innerHTML = "<p>Erro ao carregar detalhes do pedido.</p>";
  }
});
