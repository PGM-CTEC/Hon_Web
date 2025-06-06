export async function carregarProcuradores() {
  const token = localStorage.getItem("token");
  const tabelaBody = document.querySelector("table tbody");

  try {
    const response = await fetch("http://localhost:8080/relatorio", {
      method: "GET",
      headers: {
        "Authorization": "Bearer " + token
      }
    });

    console.log(response)
    if (!response.ok) throw new Error("Erro ao buscar procuradores");

    const procuradores = await response.json();
    tabelaBody.innerHTML = "";

    procuradores.forEach(p => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${p.matricula}</td>
        <td>${p.nome}</td>
        <td>${formatarCPF(p.cpf)}</td>
        <td>${formatarData(p.data_entrada)}</td>
      `;
      tabelaBody.appendChild(tr);
    });

  } catch (err) {
    console.error("Erro ao carregar procuradores:", err);
  }
}

function formatarCPF(cpf) {
  return cpf.replace(/^(\d{3})(\d{3})(\d{3})(\d{2})$/, "$1.$2.$3-$4");
}

function formatarData(data) {
  const [ano, mes, dia] = data.split("-");
  return `${dia}/${mes}/${ano}`;
}
