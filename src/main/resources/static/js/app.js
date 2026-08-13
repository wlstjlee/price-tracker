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

addForm.addEventListener("submit", handleAddSubmit);
chartCloseButton.addEventListener("click", closeHistoryChart);

loadProductList();
