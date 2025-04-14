document.addEventListener("DOMContentLoaded", () => {
    const navbarContainer = document.getElementById("navbar-container");
    if (!navbarContainer) return;

    const cliente = JSON.parse(sessionStorage.getItem("clienteLogado"));

    navbarContainer.innerHTML = `
      <div class="navbar">
        <div class="logo">
            <a href="index.html">
                <img src="assets/imagens/logo.png" alt="Logo Green Growth">
            </a>
        </div>

        <div class="menu-topo">
            ${cliente ? `
                <span id="perfil-cliente" style="cursor:pointer;">👤 Olá, ${cliente.nome.split(" ")[0]}</span>
                <a href="#" id="btnLogout" title="Sair">🚪</a>
            ` : `
                <a href="login-cliente.html">Login/Cadastre-se</a>
            `}
            <a href="carrinho.html" class="carrinho-link">
                <img src="assets/icones/carrinho.svg" alt="Carrinho">
                <span id="contador-carrinho">0</span>
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
                    window.location.href = "index.html";
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
            window.location.href = "meus-dados.html";
        });
    }
});
