let htmlOriginal = ""; // Armazena o HTML original da página principal

export async function carregarProcuradores() {
  const procuradores = await buscarProcuradores();

  const content = document.getElementById("content");

  // Salva o conteúdo original apenas na primeira vez
  if (!htmlOriginal) htmlOriginal = content.innerHTML;

  const htmlTabela = `
    <div style="padding: 20px;">
      <h2>Lista de Procuradores</h2>
      <table style="width: 100%; margin-top: 20px; border-collapse: collapse;">
        <thead style="background-color: #ccc;">
          <tr>
            <th style="padding: 10px;">Matrícula</th>
            <th style="padding: 10px;">Nome</th>
            <th style="padding: 10px;">CPF</th>
            <th style="padding: 10px;">UH</th>
          </tr>
        </thead>
        <tbody id="tabela-procuradores" style="background-color: #f9f9f9;">
          ${procuradores.map(p => `
            <tr>
              <td>${p.matricula ?? "-"}</td>
              <td>
                <a href="#" class="detalhes-procurador" data-matricula="${p.matricula}">
                  ${p.nome ?? "-"}
                </a>
              </td>
              <td>${p.cpf ?? "-"}</td>
              <td>${p.uh ?? "-"}</td>
            </tr>
          `).join("")}
        </tbody>
      </table>
    </div>
  `;

  content.innerHTML = htmlTabela;

  // Adiciona eventos aos links
  document.querySelectorAll(".detalhes-procurador").forEach(link => {
    link.addEventListener("click", e => {
      e.preventDefault();
      const matricula = link.dataset.matricula;
      mostrarDetalhesProcurador(matricula);
    });
  });
}

async function buscarProcuradores() {
  try {
    const resposta = await fetch("http://localhost:8080/relatorio", {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      }
    });

    if (!resposta.ok) {
      throw new Error("Erro ao buscar procuradores.");
    }

    return await resposta.json();
  } catch (erro) {
    console.error("Erro:", erro);
    return [];
  }
}

export async function mostrarDetalhesProcurador(matricula) {
  try {
    const [resProcurador, resAfastamentos] = await Promise.all([
      fetch(`http://localhost:8080/procurador/${matricula}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        }
      }),
      fetch(`http://localhost:8080/afastamento/${matricula}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        }
      }),
    ]);

    if (!resProcurador.ok) throw new Error("Procurador não encontrado.");
    if (!resAfastamentos.ok) throw new Error("Erro ao buscar afastamentos.");

    const procurador = await resProcurador.json();
    const afastamentos = await resAfastamentos.json();

    const content = document.getElementById("content");
    content.innerHTML = `
      <div style="padding: 20px;">
        <div style="display: flex; justify-content: space-between; align-items: center; border-radius: 8px;">
          <h2 style="margin: 0;">${procurador.nome}</h2>
          <span style="font-weight: bold;">Data de Entrada: ${procurador.data_entrada ?? "-"}</span>
        </div>

        <div class="buttons" style="margin-top: 20px;">
          <button id="btn-voltar" style="padding: 10px 20px;">Voltar</button>
          <button id="btn-adicionar" style="padding: 10px 20px;">Adicionar</button>
        </div>

        <table style="width: 100%; margin-top: 20px; border-collapse: collapse;">
          <thead style="background-color: #ccc;">
            <tr>
              <th style="padding: 10px;">Matrícula</th>
              <th style="padding: 10px;">Início do Afastamento</th>
              <th style="padding: 10px;">Fim do Afastamento</th>
              <th style="padding: 10px;">UH</th>
            </tr>
          </thead>
          <tbody id="tabela-afastamentos" style="background-color: #f9f9f9;">
            ${
              afastamentos?.length > 0
                ? afastamentos.map(a => `
                  <tr>
                    <td style="padding: 10px;">${procurador.matricula}</td>
                    <td style="padding: 10px;">${a.dataInicio ?? "-"}</td>
                    <td style="padding: 10px;">${a.dataFim ?? "-"}</td>
                    <td style="padding: 10px;">${a.uh ?? "-"}</td>
                  </tr>
                `).join('')
                : `<tr><td colspan="4" style="padding: 10px; text-align: center;">Nenhum afastamento registrado.</td></tr>`
            }
          </tbody>
        </table>
      </div>
    `;

    // Evento de "Voltar"
    document.getElementById("btn-voltar").addEventListener("click", async () => {
      // Restaura o HTML original e recarrega os procuradores
      const content = document.getElementById("content");
      content.innerHTML = htmlOriginal;
      await carregarProcuradores();
    });

    // Evento de "Adicionar"
    document.getElementById("btn-adicionar").addEventListener("click", () => {
      alert("Ação de adicionar ainda não implementada.");
    });

  } catch (erro) {
    console.error("Erro ao buscar detalhes:", erro);
    alert("Erro ao buscar detalhes do procurador.");
  }
}
