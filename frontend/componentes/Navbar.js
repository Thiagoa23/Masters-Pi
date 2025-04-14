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
                <span id="perfil-cliente" title="Meus Dados" style="cursor:pointer; display: flex; align-items: center; gap: 4px;">
                    <img src="assets/icones/perfil.svg" alt="Perfil" style="width: 24px; height: 24px;"> 
                    Olá, ${cliente.nome.split(" ")[0]}
                </span>
                <a href="#" id="btnLogout" title="Logout" style="display: inline-flex; align-items: center;">
                    <img src="assets/icones/logout.svg" alt="Logout" style="width: 24px; height: 24px;">
                </a>
            ` : `
                <a href="login-cliente.html">Login/Cadastre-se</a>
            `}
            <a href="carrinho.html" class="carrinho-link">
                <img src="assets/icones/carrinho.svg" alt="Carrinho" style="margin-top: 0.5vh;">
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