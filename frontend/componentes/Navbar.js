document.addEventListener("DOMContentLoaded", () => {
    const navbarContainer = document.getElementById("navbar-container");
    if (!navbarContainer) return;

    navbarContainer.innerHTML = `
      <div class="navbar">
        <div class="logo">
            <a href="index.html">
                <img src="assets/imagens/logo.png" alt="Logo Green Growth">
            </a>
        </div>

        <div class="menu-topo">
            <a href="login.html">Login/Cadastre-se</a>
            <a href="carrinho.html" class="carrinho-link">
                <img src="assets/icones/carrinho.svg" alt="Carrinho">
                <span id="contador-carrinho">0</span>
            </a>
        </div>
    </div>
    `;

    const contador = document.getElementById("contador-carrinho");
    const carrinho = JSON.parse(sessionStorage.getItem("carrinho")) || [];
    const total = carrinho.reduce((soma, item) => soma + item.quantidade, 0);
    contador.textContent = total;

});
