export const Estrelas = {
  renderizar(container, avaliacao) {
    container.innerHTML = "";

    const estrelasCheias = Math.floor(avaliacao);
    const temMeiaEstrela = avaliacao % 1 >= 0.5;
    const estrelasVazias = 5 - estrelasCheias - (temMeiaEstrela ? 1 : 0);

    for (let i = 0; i < estrelasCheias; i++) {
      const estrela = document.createElement("img");
      estrela.src = "assets/icones/estrela-cheia.svg";
      estrela.alt = "Estrela cheia";
      container.appendChild(estrela);
    }

    if (temMeiaEstrela) {
      const estrela = document.createElement("img");
      estrela.src = "assets/icones/estrela-meia.svg";
      estrela.alt = "Meia estrela";
      container.appendChild(estrela);
    }

    for (let i = 0; i < estrelasVazias; i++) {
      const estrela = document.createElement("img");
      estrela.src = "assets/icones/estrela-vazia.svg";
      estrela.alt = "Estrela vazia";
      container.appendChild(estrela);
    }
  }
};
