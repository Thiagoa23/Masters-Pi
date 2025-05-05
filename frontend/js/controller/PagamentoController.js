// frontend/js/controller/PagamentoController.js

document.addEventListener('DOMContentLoaded', () => {
  const paymentForm     = document.getElementById('form-pagamento');
  const cardSection     = document.getElementById('form-cartao');
  const submitButton    = document.getElementById('btn-finalizar');
  const paymentRadios   = document.querySelectorAll('input[name="formaPagamento"]');
  const cardNumberInput = document.getElementById('numero-cartao');
  const nomeTitular     = document.getElementById('nome-titular');
  const mesInput        = document.getElementById('validade-mes');
  const anoInput        = document.getElementById('validade-ano');
  const cvvInput        = document.getElementById('cvv');
  const parcelasInput   = document.getElementById('parcelas');
  const cardFields      = [cardNumberInput, nomeTitular, mesInput, anoInput, cvvInput, parcelasInput];

  // máscara ####-####-####-####
  cardNumberInput.maxLength = 19;
  cardNumberInput.addEventListener('input', e => {
    let v = e.target.value.replace(/\D/g, '').slice(0,16);
    const parts = [];
    for (let i = 0; i < v.length; i += 4) parts.push(v.substr(i,4));
    e.target.value = parts.join('-');
    updateSubmitState();
  });

  // nome sem dígitos/especiais
  nomeTitular.addEventListener('input', e => {
    e.target.value = e.target.value.replace(/[^A-Za-zÀ-ÖØ-öø-ÿ ]/g, '');
    updateSubmitState();
  });

  function validadeOK() {
    const mm = parseInt(mesInput.value,10);
    const aa = parseInt(anoInput.value,10);
    if (!mm||!aa) return false;
    const year = 2000 + aa, now = new Date();
    if (year < now.getFullYear()) return false;
    if (year === now.getFullYear() && mm < now.getMonth()+1) return false;
    return mm>=1 && mm<=12;
  }

  function toggleCardSection(show) {
    cardSection.style.display = show ? 'block' : 'none';
    cardFields.forEach(f => {
      f.disabled = !show;
      if (show) f.setAttribute('required','');
      else      f.removeAttribute('required');
    });
  }

  function updateSubmitState() {
    const sel = document.querySelector('input[name="formaPagamento"]:checked');
    if (!sel) {
      submitButton.disabled = true; return;
    }
    if (sel.value === 'boleto') {
      submitButton.disabled = false;
    } else {
      const allValid = cardFields.every(f => f.checkValidity()) && validadeOK();
      submitButton.disabled = !allValid;
    }
  }

  paymentRadios.forEach(r => r.addEventListener('change', () => {
    toggleCardSection(r.value==='cartao');
    updateSubmitState();
  }));
  cardFields.forEach(f => f.addEventListener('input', updateSubmitState));

  // init
  toggleCardSection(false);
  submitButton.disabled = true;

  paymentForm.addEventListener('submit', e => {
    e.preventDefault();
    const method = document.querySelector('input[name="formaPagamento"]:checked').value;
    sessionStorage.setItem('formaPagamento', method);

    if (method === 'cartao') {
      const pag = {
        numeroCartao: cardNumberInput.value,
        parcelas: parcelasInput.value
      };
      sessionStorage.setItem('pagamentoCartao', JSON.stringify(pag));
    } else {
      sessionStorage.removeItem('pagamentoCartao');
    }

    window.location.href = 'resumo.html';
  });
});
