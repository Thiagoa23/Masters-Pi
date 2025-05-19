// js/controller/ResumoController.js

document.addEventListener("DOMContentLoaded", async () => {
  try {
    // 1) Recupera dados da sessão
    const cliente = JSON.parse(sessionStorage.getItem("clienteLogado"));
    const carrinho = JSON.parse(sessionStorage.getItem("carrinho")) || [];
    const enderecoSelecionado = JSON.parse(sessionStorage.getItem("enderecoSelecionado"));
    const formaPagamento = sessionStorage.getItem("formaPagamento");
    const pagamentoCartao = JSON.parse(sessionStorage.getItem("pagamentoCartao") || "{}");
    const numeroCartao = pagamentoCartao.numeroCartao;
    const parcelas = pagamentoCartao.parcelas;

    if (!cliente?.id) {
      alert("Você precisa estar logado para continuar o checkout.");
      return window.location.href = "../login-cliente.html";
    }

    // 2) Renderiza Endereço de Entrega
    const enderecoInfo = document.getElementById("endereco-info");
    enderecoInfo.textContent = enderecoSelecionado
      ? `${enderecoSelecionado.logradouro}, ${enderecoSelecionado.numero} – ${enderecoSelecionado.bairro}, ${enderecoSelecionado.cidade}/${enderecoSelecionado.uf}, CEP: ${enderecoSelecionado.cep}`
      : "Nenhum endereço selecionado.";

    // 3) Renderiza Forma de Pagamento
    const pagamentoInfo = document.getElementById("pagamento-info");
    const parcelasInfo = document.getElementById("parcelas-info");
    if (formaPagamento === "boleto") {
      pagamentoInfo.textContent = "Boleto Bancário";
      parcelasInfo.style.display = "none";
    } else {
      const final4 = numeroCartao?.slice(-4) || "----";
      pagamentoInfo.textContent = `Cartão de Crédito – Final ${final4}`;
      parcelasInfo.textContent = `Parcelas: ${parcelas}×`;
      parcelasInfo.style.display = "block";
    }

    // 4) Renderiza frete e totals
    const radios = document.querySelectorAll('input[name="frete"]');
    let freteSelecionado = parseFloat(sessionStorage.getItem("freteSelecionado") || radios[0].value);

    function atualizarTotais() {
      const subtotal = carrinho.reduce((acc, item) => {
        const preco = item.produto?.preco ?? item.valor;
        return acc + preco * item.quantidade;
      }, 0);
      document.getElementById("subtotal").textContent = subtotal.toFixed(2);
      document.getElementById("frete-valor").textContent = freteSelecionado.toFixed(2);
      document.getElementById("total-final").textContent = (subtotal + freteSelecionado).toFixed(2);
      sessionStorage.setItem("freteSelecionado", freteSelecionado);
    }
    radios.forEach(radio => {
      if (parseFloat(radio.value) === freteSelecionado) radio.checked = true;
      radio.addEventListener("change", () => {
        freteSelecionado = parseFloat(radio.value);
        atualizarTotais();
      });
    });
    atualizarTotais();

    // 5) Renderiza lista de produtos (mesma lógica que antes)
    const lista = document.getElementById("lista-produtos");
    lista.innerHTML = "";
    carrinho.forEach(item => {
      const prod = item.produto || {};
      const nome = prod.nome || item.nome;
      const preco = prod.preco ?? item.valor;
      const quantidade = item.quantidade;
      const totalItem = (preco * quantidade).toFixed(2);
      const imgName = (prod.imagens?.[0]?.url) || item.imagem || "placeholder.png";
      const src = `http://localhost:8080/uploads/${imgName}`;

      lista.insertAdjacentHTML("beforeend", `
        <div class="produto-item">
          <img src="${src}"
               alt="${nome}"
               class="resumo-img"
               onerror="this.src='http://localhost:8080/uploads/placeholder.png';"/>
          <div class="item-dados">
            <h4>${nome}</h4>
            <p>R$ ${preco.toFixed(2)} × ${quantidade}</p>
          </div>
          <div class="item-total">R$ ${totalItem}</div>
        </div>
      `);
    });

    // 6) Botão Voltar
    document.getElementById("btn-voltar")
      .addEventListener("click", () => window.location.href = "pagamento.html");

    // 7) Botão Concluir Compra — **AQUI É A MUDANÇA PRINCIPAL**
    document.getElementById("btn-concluir").addEventListener("click", async () => {
      const itensDTO = carrinho.map(item => ({
        produtoId: item.produto?.id ?? item.codigo,
        nome: item.produto?.nome ?? item.nome,
        preco: item.produto?.preco ?? item.valor,
        quantidade: item.quantidade
      }));
      const subtotal = carrinho.reduce((acc, item) =>
        acc + ((item.produto?.preco ?? item.valor) * item.quantidade), 0);
      const valorTotal = subtotal + freteSelecionado;

      const pedido = {
        clienteId: cliente.id,
        enderecoEntrega: enderecoSelecionado,
        itens: itensDTO,
        formaPagamento,
        valorFrete: freteSelecionado,
        valorTotal
      };

      const resp = await fetch("http://localhost:8080/api/pedido", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(pedido)
      });
      if (!resp.ok) throw new Error("Falha ao concluir pedido");

      const data = await resp.json();
      alert(`Pedido nº ${data.numeroPedido} criado com sucesso!\nTotal: R$ ${data.valorTotal.toFixed(2)}`);

      // Limpa apenas dados de checkout (login permanece)
      sessionStorage.removeItem("carrinho");
      sessionStorage.removeItem("enderecoSelecionado");
      sessionStorage.removeItem("freteSelecionado");
      sessionStorage.removeItem("formaPagamento");
      sessionStorage.removeItem("pagamentoCartao");

      window.location.href = "../index.html";
    });

  } catch (e) {
    console.error(e);
    alert("Erro ao carregar o resumo do pedido.");
  }
});
