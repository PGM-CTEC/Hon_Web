// Função para carregar um componente HTML externo
async function loadComponent(id, path, callback) {
  const container = document.getElementById(id);
  if (!container) return;

  try {
    const response = await fetch(path);
    const html = await response.text();
    container.innerHTML = html;

    // Se tiver callback (ex: ativar logout), executa
    if (typeof callback === "function") {
      callback();
    }

  } catch (error) {
    console.error(`Erro ao carregar componente ${id}:`, error);
  }
}

// Ativa funcionalidade de logout
function ativarLogout() {
  const logoutBtn = document.getElementById("logout");

  if (logoutBtn) {
    logoutBtn.addEventListener("click", () => {
      localStorage.removeItem("token");
      window.location.href = "../pages/login.html";
    });
  } else {
    console.warn("Botão #logout não encontrado.");
  }
}

// Ao carregar a página, carrega os componentes
window.addEventListener("DOMContentLoaded", () => {
  loadComponent("sidebar-container", "../components/sidebar.html");
  loadComponent("topbar-container", "../components/topbar.html", ativarLogout); // ⬅️ callback aqui
});
