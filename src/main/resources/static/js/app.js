const authView = document.getElementById("auth-view");
const appView = document.getElementById("app-view");

const loginTabButton = document.getElementById("login-tab-button");
const signupTabButton = document.getElementById("signup-tab-button");
const loginForm = document.getElementById("login-form");
const signupForm = document.getElementById("signup-form");
const loginEmailInput = document.getElementById("login-email");
const loginPasswordInput = document.getElementById("login-password");
const loginButton = document.getElementById("login-button");
const loginError = document.getElementById("login-error");
const signupNameInput = document.getElementById("signup-name");
const signupEmailInput = document.getElementById("signup-email");
const signupPasswordInput = document.getElementById("signup-password");
const signupButton = document.getElementById("signup-button");
const signupError = document.getElementById("signup-error");
const signupSuccess = document.getElementById("signup-success");

const userInfoEl = document.getElementById("user-info");
const logoutButton = document.getElementById("logout-button");

const addForm = document.getElementById("add-form");
const urlInput = document.getElementById("url-input");
const addButton = document.getElementById("add-button");
const addError = document.getElementById("add-error");

const loadingEl = document.getElementById("loading");
const emptyMessageEl = document.getElementById("empty-message");
const productListEl = document.getElementById("product-list");
const productCardTemplate = document.getElementById("product-card-template");

const chartSection = document.getElementById("chart-section");
const chartTitle = document.getElementById("chart-title");
const chartCloseButton = document.getElementById("chart-close");
const chartEmptyEl = document.getElementById("chart-empty");
const chartCanvas = document.getElementById("price-chart");

let priceChart = null;

const priceFormatter = new Intl.NumberFormat("ko-KR");

function formatPrice(price) {
  return `${priceFormatter.format(price)}원`;
}

function formatDateTime(isoString) {
  const date = new Date(isoString);
  return date.toLocaleString("ko-KR", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
  });
}

function showAddError(message) {
  addError.textContent = message;
  addError.classList.remove("hidden");
}

function clearAddError() {
  addError.textContent = "";
  addError.classList.add("hidden");
}

function renderProductList(products) {
  productListEl.innerHTML = "";

  if (products.length === 0) {
    emptyMessageEl.classList.remove("hidden");
    return;
  }
  emptyMessageEl.classList.add("hidden");

  products.forEach((product) => {
    const node = productCardTemplate.content.cloneNode(true);

    const imageEl = node.querySelector(".product-image");
    imageEl.alt = product.name || "상품 이미지";
    if (product.imageUrl) {
      imageEl.src = product.imageUrl;
    } else {
      imageEl.style.visibility = "hidden";
    }
    imageEl.addEventListener("error", () => {
      imageEl.style.visibility = "hidden";
    });

    node.querySelector(".product-mall").textContent = product.mallName || "";
    node.querySelector(".product-name").textContent = product.name || "(상품명 없음)";
    node.querySelector(".product-price").textContent = formatPrice(product.currentLowestPrice);

    const linkEl = node.querySelector(".product-link");
    linkEl.href = product.url;

    node.querySelector(".history-button").addEventListener("click", () => {
      openHistoryChart(product);
    });

    node.querySelector(".delete-button").addEventListener("click", () => {
      handleDelete(product);
    });

    productListEl.appendChild(node);
  });
}

async function loadProductList() {
  loadingEl.classList.remove("hidden");
  emptyMessageEl.classList.add("hidden");
  try {
    const products = await api.getInterests();
    renderProductList(products);
  } catch (err) {
    showAddError(err.message);
  } finally {
    loadingEl.classList.add("hidden");
  }
}

async function handleAddSubmit(event) {
  event.preventDefault();
  clearAddError();

  const url = urlInput.value.trim();
  if (!url) return;

  addButton.disabled = true;
  addButton.textContent = "등록 중...";

  try {
    await api.createInterest(url);
    urlInput.value = "";
    await loadProductList();
  } catch (err) {
    showAddError(err.message);
  } finally {
    addButton.disabled = false;
    addButton.textContent = "등록";
  }
}

async function handleDelete(product) {
  const confirmed = confirm(`"${product.name || product.url}"을(를) 삭제할까요?`);
  if (!confirmed) return;

  try {
    await api.deleteInterest(product.id);
    if (chartSection.dataset.productId === String(product.id)) {
      closeHistoryChart();
    }
    await loadProductList();
  } catch (err) {
    showAddError(err.message);
  }
}

async function openHistoryChart(product) {
  chartSection.classList.remove("hidden");
  chartSection.dataset.productId = String(product.id);
  chartTitle.textContent = `가격 변동 이력 - ${product.name || product.url}`;
  chartSection.scrollIntoView({ behavior: "smooth", block: "start" });

  try {
    const histories = await api.getHistories(product.id);
    renderChart(histories);
  } catch (err) {
    showAddError(err.message);
  }
}

function closeHistoryChart() {
  chartSection.classList.add("hidden");
  delete chartSection.dataset.productId;
  if (priceChart) {
    priceChart.destroy();
    priceChart = null;
  }
}

function renderChart(histories) {
  if (priceChart) {
    priceChart.destroy();
    priceChart = null;
  }

  if (!histories || histories.length === 0) {
    chartEmptyEl.classList.remove("hidden");
    chartCanvas.classList.add("hidden");
    return;
  }
  chartEmptyEl.classList.add("hidden");
  chartCanvas.classList.remove("hidden");

  const sorted = [...histories].sort(
    (a, b) => new Date(a.recordedAt) - new Date(b.recordedAt)
  );

  priceChart = new Chart(chartCanvas, {
    type: "line",
    data: {
      labels: sorted.map((h) => formatDateTime(h.recordedAt)),
      datasets: [
        {
          label: "가격",
          data: sorted.map((h) => h.price),
          borderColor: "#111111",
          backgroundColor: "rgba(17, 17, 17, 0.06)",
          tension: 0.2,
          fill: true,
          pointRadius: 3,
        },
      ],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { display: false },
        tooltip: {
          callbacks: {
            label: (context) => formatPrice(context.parsed.y),
          },
        },
      },
      scales: {
        y: {
          ticks: {
            callback: (value) => formatPrice(value),
          },
        },
      },
    },
  });
}

function showLoginTab() {
  loginTabButton.classList.add("active");
  signupTabButton.classList.remove("active");
  loginForm.classList.remove("hidden");
  signupForm.classList.add("hidden");
  signupSuccess.classList.add("hidden");
}

function showSignupTab() {
  signupTabButton.classList.add("active");
  loginTabButton.classList.remove("active");
  signupForm.classList.remove("hidden");
  loginForm.classList.add("hidden");
}

function showAuthView(message) {
  authView.classList.remove("hidden");
  appView.classList.add("hidden");
  closeHistoryChart();
  showLoginTab();
  if (message) {
    showLoginError(message);
  } else {
    clearLoginError();
  }
}

function showAppView() {
  const user = auth.getCurrentUser();
  userInfoEl.textContent = user ? `${user.name || user.email} 님` : "";
  authView.classList.add("hidden");
  appView.classList.remove("hidden");
  loadProductList();
}

function showLoginError(message) {
  loginError.textContent = message;
  loginError.classList.remove("hidden");
}

function clearLoginError() {
  loginError.textContent = "";
  loginError.classList.add("hidden");
}

function showSignupError(message) {
  signupError.textContent = message;
  signupError.classList.remove("hidden");
}

function clearSignupError() {
  signupError.textContent = "";
  signupError.classList.add("hidden");
}

async function handleLoginSubmit(event) {
  event.preventDefault();
  clearLoginError();

  const email = loginEmailInput.value.trim();
  const password = loginPasswordInput.value;
  if (!email || !password) return;

  loginButton.disabled = true;
  loginButton.textContent = "로그인 중...";

  try {
    const { token, email: loggedInEmail, name } = await authApi.login({ email, password });
    auth.setSession(token, loggedInEmail, name);
    loginForm.reset();
    showAppView();
  } catch (err) {
    showLoginError(err.message);
  } finally {
    loginButton.disabled = false;
    loginButton.textContent = "로그인";
  }
}

async function handleSignupSubmit(event) {
  event.preventDefault();
  clearSignupError();
  signupSuccess.classList.add("hidden");

  const name = signupNameInput.value.trim();
  const email = signupEmailInput.value.trim();
  const password = signupPasswordInput.value;
  if (!name || !email || !password) return;

  signupButton.disabled = true;
  signupButton.textContent = "가입 중...";

  try {
    await authApi.signup({ email, password, name });
    signupForm.reset();
    showLoginTab();
    signupSuccess.textContent = "회원가입이 완료되었습니다. 로그인해주세요.";
    signupSuccess.classList.remove("hidden");
    loginEmailInput.value = email;
  } catch (err) {
    showSignupError(err.message);
  } finally {
    signupButton.disabled = false;
    signupButton.textContent = "회원가입";
  }
}

function handleLogout() {
  auth.clearSession();
  showAuthView();
}

auth.setUnauthorizedHandler(() => showAuthView("로그인이 필요합니다. 다시 로그인해주세요."));

loginTabButton.addEventListener("click", showLoginTab);
signupTabButton.addEventListener("click", showSignupTab);
loginForm.addEventListener("submit", handleLoginSubmit);
signupForm.addEventListener("submit", handleSignupSubmit);
logoutButton.addEventListener("click", handleLogout);

addForm.addEventListener("submit", handleAddSubmit);
chartCloseButton.addEventListener("click", closeHistoryChart);

if (auth.getToken()) {
  showAppView();
} else {
  showAuthView();
}
