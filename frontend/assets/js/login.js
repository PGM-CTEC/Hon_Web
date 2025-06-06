document.getElementById("loginForm").addEventListener("submit", async function (e) {
  e.preventDefault();

  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;

  try {
    const resposta = await fetch("http://localhost:8080/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ username, password }),
      credentials: "include" // se você for usar cookies (opcional)
    });

    const contentType = resposta.headers.get("content-type");

    console.log(resposta)
    if (!resposta.ok) {
      // Verifica se a resposta é JSON para mostrar mensagem de erro
      let erroMsg = "Erro desconhecido";
      if (contentType && contentType.includes("application/json")) {
        const erro = await resposta.json();
        erroMsg = erro.mensagem || erro.error || JSON.stringify(erro);
      } else {
        const erroTexto = await resposta.text();
        erroMsg = erroTexto || "Erro inesperado";
      }
      throw new Error(erroMsg);
    }

    // Garante que a resposta é JSON antes de usar
    if (contentType && contentType.includes("application/json")) {
      const dados = await resposta.json();

      // Armazena o token (pode usar cookie se preferir)
      localStorage.setItem("token", dados.token);

      // Mensagem de sucesso
      document.getElementById("mensagem").textContent = "Login realizado com sucesso!";
      document.getElementById("mensagem").style.color = "green";

      // Redireciona para outra página
      setTimeout(() => {
        window.location.href = "home.html";
      }, 1000);
    } else {
      throw new Error("Resposta do servidor não está no formato JSON.");
    }
  } catch (erro) {
    document.getElementById("mensagem").textContent = erro.message;
    document.getElementById("mensagem").style.color = "red";
  }
});
