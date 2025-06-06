export async function validarToken() {
  const token = localStorage.getItem("token");

  if (!token) {
    window.location.href = "../pages/login.html";
    return;
  }

  try {
    const response = await fetch(`http://localhost:8080/auth/validate?token=${token}`, {
      method: "GET",
      headers: {
        "Authorization": "Bearer " + token
      }
    });

    if (!response.ok) throw new Error("Token inválido");

    const valido = await response.json();
    if (!valido) {
      window.location.href = "../pages/login.html";
    }

  } catch (err) {
    console.error("Erro ao validar token:", err);
    window.location.href = "../pages/login.html";
  }
}
