document.addEventListener("DOMContentLoaded", () => {
    const navbarContainer = document.getElementById("navbar-container");
    if (!navbarContainer) return;

    const cliente = JSON.parse(sessionStorage.getItem("clienteLogado"));

    // Corrige caminho relativo dependendo da pasta atual
    const basePath = window.location.pathname.includes("/checkout/")
        ? "../"
        : "";

    navbarContainer.innerHTML = `
      <div class="navbar">
        <div class="logo">
            <a href="${basePath}index.html">
                <img src="${basePath}assets/imagens/logo.png" alt="Logo Green Growth">
            </a>
        </div>

        <div class="menu-topo">
            ${cliente ? `
                <span id="perfil-cliente" title="Meus Dados" style="cursor:pointer; display: flex; align-items: center; gap: 4px;">
                    <img src="${basePath}assets/icones/perfil.svg" alt="Perfil" style="width: 24px; height: 24px;"> 
                    Olá, ${cliente.nome.split(" ")[0]}
                </span>
                <a href="${basePath}pedidos.html" title="Meus Pedidos" style="display: inline-flex; align-items: center; gap: 4px;">
                    <img src="${basePath}assets/icones/box.svg" alt="Pedidos" style="width: 24px; height: 24px;">
                    <span>Meus Pedidos</span>
                </a>
                <a href="#" id="btnLogout" title="Logout" style="display: inline-flex; align-items: center;">
                    <img src="${basePath}assets/icones/logout.svg" alt="Logout" style="width: 24px; height: 24px;">
                </a>

            ` : `
                <a href="${basePath}login-cliente.html">Login/Cadastre-se</a>
            `}
            <a href="${basePath}carrinho.html" class="carrinho-link">
                <img src="${basePath}assets/icones/carrinho.svg" alt="Carrinho" style="margin-top: 0.5vh;">
                <span id="contador-carrinho" style="margin-top: 0.5vh;">0</span>
            </a>
        </div>
      </div>
    `;

    // Atualiza contador do carrinho
    const contador = document.getElementById("contador-carrinho");
    const carrinho = JSON.parse(sessionStorage.getItem("carrinho")) || [];
    const total = carrinho.reduce((soma, item) => soma + item.quantidade, 0);
    contador.textContent = total;

    // Logout
    const btnLogout = document.getElementById("btnLogout");
    if (btnLogout) {
        btnLogout.addEventListener("click", async () => {
            try {
                const response = await fetch("http://localhost:8080/api/cliente/logout", {
                    method: "GET",
                    credentials: "include"
                });

                if (response.ok) {
                    sessionStorage.removeItem("clienteLogado");
                    window.location.href = `${basePath}index.html`;
                }
            } catch (err) {
                alert("Erro ao sair. Tente novamente.");
            }
        });
    }

    // Redireciona para a tela de dados
    const perfil = document.getElementById("perfil-cliente");
    if (perfil) {
        perfil.addEventListener("click", () => {
            window.location.href = `${basePath}meus-dados.html`;
        });
    }
});
