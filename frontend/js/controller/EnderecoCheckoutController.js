// js/controller/EnderecoCheckoutController.js

document.addEventListener("DOMContentLoaded", async () => {
  const clienteStorage = sessionStorage.getItem("clienteLogado");
  if (!clienteStorage) {
    alert("Você precisa estar logado para continuar o checkout.");
    return window.location.href = "../login-cliente.html";
  }
  let cliente = JSON.parse(clienteStorage);

  const lista           = document.getElementById("enderecos-lista");
  const btnAdd          = document.getElementById("btn-adicionar-endereco");
  const btnContinuar    = document.getElementById("btn-continuar");
  const modal           = document.getElementById("modalEndereco");
  const form            = document.getElementById("formEndereco");
  const btnCancelar     = document.querySelector(".btn-cancelar");
  const msgCEP          = document.getElementById("mensagemEndereco");

  // 1) Recarrega dados do cliente (e endereços) do backend
  async function carregarEnderecos() {
    lista.innerHTML = "<p>Carregando endereços...</p>";
    const resp = await fetch(
      `http://localhost:8080/api/cliente/buscar?email=${encodeURIComponent(cliente.email)}`,
      { credentials: "include" }
    );
    if (!resp.ok) {
      lista.innerHTML = "<p>Erro ao carregar endereços.</p>";
      return;
    }
    cliente = await resp.json();
    sessionStorage.setItem("clienteLogado", JSON.stringify(cliente));

    // 2) Renderiza cards
    lista.innerHTML = "";
    cliente.enderecosEntrega.forEach((end) => {
      const div = document.createElement("div");
      div.className = "endereco-card";
      div.innerHTML = `
        <p><strong>${end.logradouro}, ${end.numero}${end.complemento ? `, ${end.complemento}` : ""}</strong></p>
        <p>${end.bairro} – ${end.cidade}/${end.uf}</p>
        <p>CEP: ${end.cep}</p>
      `;
      div.addEventListener("click", () => {
        sessionStorage.setItem("enderecoSelecionado", JSON.stringify(end));
        document.querySelectorAll(".endereco-card")
                .forEach(c => c.classList.remove("selecionado"));
        div.classList.add("selecionado");
        btnContinuar.disabled = false;
      });
      lista.appendChild(div);
    });

    // reaplica seleção anterior
    const sel = sessionStorage.getItem("enderecoSelecionado");
    if (sel) {
      const eSel = JSON.parse(sel);
      document.querySelectorAll(".endereco-card").forEach((c, i) => {
        if (cliente.enderecosEntrega[i]?.id === eSel.id) {
          c.classList.add("selecionado");
          btnContinuar.disabled = false;
        }
      });
    }
  }

  // 3) Abre / fecha modal
  btnAdd.addEventListener("click", () => modal.style.display = "flex");
  btnCancelar.addEventListener("click", () => {
    modal.style.display = "none";
    form.reset();
    msgCEP.innerText = "";
  });

  // 4) Máscara e auto-preenchimento de CEP
  form.cep.addEventListener("input", () => {
    let cep = form.cep.value.replace(/\D/g, "");
    if (cep.length > 5) cep = cep.slice(0,5) + "-" + cep.slice(5);
    form.cep.value = cep.slice(0,9);
  });
  form.cep.addEventListener("blur", async () => {
    const cep = form.cep.value.replace(/\D/g, "");
    msgCEP.innerText = "";
    if (cep.length !== 8) return;
    try {
      const r = await fetch(`https://viacep.com.br/ws/${cep}/json/`);
      const d = await r.json();
      if (d.erro) { msgCEP.innerText = "CEP não encontrado."; return; }
      form.logradouro.value = d.logradouro;
      form.bairro.value    = d.bairro;
      form.cidade.value    = d.localidade;
      form.uf.value        = d.uf;
    } catch {
      msgCEP.innerText = "Erro ao consultar o CEP.";
    }
  });

  // 5) Salva endereço e recarrega lista (sem redirecionar)
  form.addEventListener("submit", async e => {
    e.preventDefault();
    msgCEP.innerText = "";
    const cep = form.cep.value.replace(/\D/g, "");
    if (cep.length !== 8) { msgCEP.innerText = "CEP inválido."; return; }

    const novo = {
      cep,
      logradouro: form.logradouro.value.trim(),
      numero:     form.numero.value.trim(),
      complemento:form.complemento.value.trim(),
      bairro:     form.bairro.value.trim(),
      cidade:     form.cidade.value.trim(),
      uf:         form.uf.value.trim().toUpperCase()
    };

    const resp = await fetch("http://localhost:8080/api/cliente/endereco", {
      method:      "POST",
      headers:     { "Content-Type": "application/json" },
      credentials: "include",
      body:        JSON.stringify(novo)
    });
    if (!resp.ok) {
      const err = await resp.text();
      msgCEP.innerText = err || "Erro ao salvar endereço.";
      return;
    }

    // fecha modal e recarrega lista
    modal.style.display = "none";
    form.reset();
    await carregarEnderecos();
  });

  // 6) Continua para pagamento
  btnContinuar.addEventListener("click", () => {
    window.location.href = "pagamento.html";
  });

  // 7) Inicialização
  await carregarEnderecos();
});
