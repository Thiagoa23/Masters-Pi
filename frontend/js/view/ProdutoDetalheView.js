export const ProdutoDetalheView = {
  renderizar(produto) {
    document.getElementById("nome-produto").textContent = produto.nome;
    document.getElementById("preco-produto").textContent = `R$ ${produto.valor.toFixed(2)}`;
    document.getElementById("descricao-produto").textContent = produto.descricao;
  
    const imagensUnicas = [...new Set([produto.imagemPrincipal, ...produto.imagens.filter(img => img !== produto.imagemPrincipal)])];
  
    this.renderizarEstrelas(produto.avaliacao, "avaliacao");
    this.renderizarCarrossel(imagensUnicas);
    this.renderizarMiniaturas(imagensUnicas);
  },

  renderizarEstrelas(avaliacao, containerId) {
    const container = document.getElementById(containerId);
    container.innerHTML = "";

    const cheias = Math.floor(avaliacao);
    const meia = avaliacao % 1 >= 0.5;
    const vazias = 5 - cheias - (meia ? 1 : 0);

    for (let i = 0; i < cheias; i++) {
      const img = document.createElement("img");
      img.src = "assets/icones/estrela-cheia.svg";
      container.appendChild(img);
    }

    if (meia) {
      const img = document.createElement("img");
      img.src = "assets/icones/estrela-meia.svg";
      container.appendChild(img);
    }

    for (let i = 0; i < vazias; i++) {
      const img = document.createElement("img");
      img.src = "assets/icones/estrela-vazia.svg";
      container.appendChild(img);
    }
  },

  renderizarCarrossel(imagens) {
    const slides = document.getElementById("glide-slides");
    slides.innerHTML = "";
  
    imagens.forEach((img, index) => {
      slides.innerHTML += `
        <li class="glide__slide">
          <img src="http://localhost:8080/uploads/${img}" alt="Imagem ${index + 1}">
        </li>`;
    });
  
    if (window.glide) window.glide.destroy();
  
    window.glide = new Glide('.glide', {
      type: 'carousel',
      autoplay: 4000,
      hoverpause: true,
      perView: 1
    });
  
    // Marca miniatura ativa conforme autoplay
    window.glide.on('run', () => {
      const indiceAtual = window.glide.index;
      document.querySelectorAll(".miniaturas img").forEach((img, i) => {
        img.classList.toggle("selecionada", i === indiceAtual);
      });
    });
  
    window.glide.mount();
  },

  renderizarMiniaturas(imagens) {
    const miniaturasEl = document.querySelector(".miniaturas");
    miniaturasEl.innerHTML = "";
  
    imagens.forEach((img, index) => {
      const thumb = document.createElement("img");
      thumb.src = `http://localhost:8080/uploads/${img}`;
      thumb.alt = "Miniatura";
  
      // Marca a primeira como selecionada inicialmente
      if (index === 0) thumb.classList.add("selecionada");
  
      thumb.addEventListener("click", () => {
        window.glide.go(`=${index}`);
        document.querySelectorAll(".miniaturas img").forEach(img => img.classList.remove("selecionada"));
        thumb.classList.add("selecionada");
      });
  
      miniaturasEl.appendChild(thumb);
    });
  }
  
};
