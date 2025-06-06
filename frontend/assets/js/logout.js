document.addEventListener("DOMContentLoaded", () => {
  const logoutBtn = document.getElementById("logout");

  if (logoutBtn) {
    logoutBtn.addEventListener("click", () => {
      localStorage.removeItem("token");

    setTimeout(() => {
        window.location.href = "../pages/login.html";
      }, 1000);
      
    });
  }
});
