import { atualizarContadorCarrinho } from "../../utils/CarrinhoUtils.js";

document.addEventListener("DOMContentLoaded", () => {
  const listaEl = document.getElementById("lista-carrinho");
  const totalEl = document.getElementById("total-geral");
  const subtotalEl = document.getElementById("subtotal");
  const freteSection = document.querySelector(".frete");
  const erroCep = document.getElementById("erro-cep");
  const cepInput = document.getElementById("cep");
  const cepWrapper = document.querySelector(".cep");
  const totalWrapper = document.getElementById("wrapper-total");
  const botaoFinalizar = document.getElementById("finalizar-compra");

  let carrinho = JSON.parse(sessionStorage.getItem("carrinho")) || [];
  let cepValido = false;

  const fretesPorRegiao = {
    "sudeste": [
      { nome: "Jadlog Econômico", valor: 7.80, prazo: "5-7 dias úteis" },
      { nome: "Correios PAC", valor: 11.40, prazo: "3-5 dias úteis" },
      { nome: "Loggi Rápido", valor: 19.90, prazo: "1-2 dias úteis" }
    ],
    "sul": [
      { nome: "Coleta Já", valor: 6.50, prazo: "5-7 dias úteis" },
      { nome: "Transporta Sul", valor: 10.80, prazo: "3-4 dias úteis" },
      { nome: "FlashGo", valor: 18.00, prazo: "1-2 dias úteis" }
    ],
    "nordeste": [
      { nome: "NordExpress", valor: 9.90, prazo: "6-8 dias úteis" },
      { nome: "Correios PAC", valor: 13.60, prazo: "3-5 dias úteis" },
      { nome: "Loggi Expresso", valor: 21.90, prazo: "1-3 dias úteis" }
    ],
    "norte": [
      { nome: "Amazônia Cargo", valor: 12.90, prazo: "7-10 dias úteis" },
      { nome: "Correios PAC", valor: 15.90, prazo: "4-6 dias úteis" },
      { nome: "Super Flash", valor: 25.00, prazo: "2-3 dias úteis" }
    ],
    "centro-oeste": [
      { nome: "Rápido Central", valor: 8.50, prazo: "5-7 dias úteis" },
      { nome: "Correios PAC", valor: 12.90, prazo: "3-5 dias úteis" },
      { nome: "JetLog", valor: 20.00, prazo: "1-2 dias úteis" }
    ]
  };

  function identificarRegiao(cep) {
    const inicio = parseInt(cep.charAt(0));
    if ([0, 1, 2].includes(inicio)) return "sudeste";
    if (inicio === 3) return "sul";
    if ([4, 5].includes(inicio)) return "centro-oeste";
    if ([6, 7].includes(inicio)) return "nordeste";
    if ([8, 9].includes(inicio)) return "norte";
    return null;
  }

  function renderizarFretes(regiao) {
    const opcoes = fretesPorRegiao[regiao];
    freteSection.innerHTML = "<h2>Escolha o frete</h2>";

    opcoes.forEach((opcao, index) => {
      const radio = document.createElement("input");
      radio.type = "radio";
      radio.name = "frete";
      radio.value = opcao.valor;
      if (index === 0) radio.checked = true;

      const label = document.createElement("label");
      label.appendChild(radio);
      label.innerHTML += ` ${opcao.nome} - R$${opcao.valor.toFixed(2)} (${opcao.prazo})`;

      freteSection.appendChild(label);
    });

    document.querySelectorAll('input[name="frete"]').forEach(radio => {
      radio.addEventListener("change", atualizarTotal);
    });
  }

  function salvarCarrinho() {
    sessionStorage.setItem("carrinho", JSON.stringify(carrinho));
  }

  function atualizarTotal() {
    const freteSelecionado = parseFloat(document.querySelector('input[name="frete"]:checked')?.value || 0);
    const subtotalProdutos = carrinho.reduce((total, item) => total + item.valor * item.quantidade, 0);

    if (cepValido) {
      subtotalEl.textContent = `Subtotal: R$ ${subtotalProdutos.toFixed(2)} + R$ ${freteSelecionado.toFixed(2)}`;
      totalEl.textContent = (subtotalProdutos + freteSelecionado).toFixed(2);
    } else {
      subtotalEl.textContent = `Subtotal: R$ ${subtotalProdutos.toFixed(2)}`;
      totalEl.textContent = subtotalProdutos.toFixed(2);
    }
  }

  function renderizarCarrinho() {
    listaEl.innerHTML = "";

    if (carrinho.length === 0) {
      listaEl.innerHTML = "<p>Seu carrinho está vazio.</p>";
      totalEl.textContent = "0,00";
      subtotalEl.textContent = "0,00";
      freteSection.style.display = "none";
      totalWrapper.style.display = "none";
      botaoFinalizar.style.display = "none";
      cepWrapper.style.display = "none";
      return;
    }

    cepWrapper.style.display = "block";
    totalWrapper.style.display = "block";
    botaoFinalizar.style.display = "block";
    if (cepValido) freteSection.style.display = "block";

    carrinho.forEach((item, index) => {
      const div = document.createElement("div");
      div.className = "item-carrinho";

      div.innerHTML = `
        <img src="http://localhost:8080/uploads/${item.imagem}" alt="${item.nome}" />
        <div class="info">
          <h3>${item.nome}</h3>
          <p>Preço: R$ ${item.valor.toFixed(2)}</p>
          <p>Subtotal: R$ ${(item.valor * item.quantidade).toFixed(2)}</p>
          <div class="quantidade">
            <button class="diminuir">-</button>
            <span>${item.quantidade}</span>
            <button class="aumentar">+</button>
            <button class="remover">Remover</button>
          </div>
        </div>
      `;

      div.querySelector(".aumentar").addEventListener("click", () => {
        item.quantidade++;
        salvarCarrinho();
        atualizarContadorCarrinho();
        renderizarCarrinho();
      });

      div.querySelector(".diminuir").addEventListener("click", () => {
        if (item.quantidade > 1) {
          item.quantidade--;
          salvarCarrinho();
          atualizarContadorCarrinho();
          renderizarCarrinho();
        }
      });

      div.querySelector(".remover").addEventListener("click", () => {
        carrinho.splice(index, 1);
        salvarCarrinho();
        atualizarContadorCarrinho();
        renderizarCarrinho();
      });

      listaEl.appendChild(div);
    });

    atualizarTotal();
  }

  document.getElementById("calcular-frete").addEventListener("click", () => {
    const cep = cepInput.value.trim().replace(/\D/g, "");

    if (cep.length !== 8) {
      erroCep.textContent = "CEP deve conter 8 dígitos.";
      erroCep.style.display = "block";
      freteSection.style.display = "none";
      cepValido = false;
      atualizarTotal();
      return;
    }

    fetch(`https://viacep.com.br/ws/${cep}/json/`)
      .then(response => response.json())
      .then(data => {
        if (data.erro) {
          erroCep.textContent = "CEP não encontrado.";
          erroCep.style.display = "block";
          freteSection.style.display = "none";
          cepValido = false;
        } else {
          const regiao = identificarRegiao(cep);
          if (regiao && fretesPorRegiao[regiao]) {
            renderizarFretes(regiao);
            erroCep.style.display = "none";
            freteSection.style.display = "block";
            cepValido = true;
          } else {
            erroCep.textContent = "Região inválida para cálculo de frete.";
            erroCep.style.display = "block";
            freteSection.style.display = "none";
            cepValido = false;
          }
        }
        atualizarTotal();
      })
      .catch(() => {
        erroCep.textContent = "Erro ao validar o CEP.";
        erroCep.style.display = "block";
        freteSection.style.display = "none";
        cepValido = false;
        atualizarTotal();
      });
  });

  cepInput.addEventListener("input", (e) => {
    let cep = e.target.value.replace(/\D/g, "");
    if (cep.length > 5) {
      cep = cep.replace(/^(\d{5})(\d{1,3})/, "$1-$2");
    }
    e.target.value = cep.slice(0, 9);
  });

  renderizarCarrinho();
});
