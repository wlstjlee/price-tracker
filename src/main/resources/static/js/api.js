const API_BASE = "/api/interests";
const AUTH_BASE = "/api/auth";

const AUTH_TOKEN_KEY = "pt_token";
const AUTH_EMAIL_KEY = "pt_email";
const AUTH_NAME_KEY = "pt_name";

let onUnauthorized = null;

function getToken() {
  return localStorage.getItem(AUTH_TOKEN_KEY);
}

function getCurrentUser() {
  const email = localStorage.getItem(AUTH_EMAIL_KEY);
  if (!email) return null;
  return { email, name: localStorage.getItem(AUTH_NAME_KEY) };
}

function setSession(token, email, name) {
  localStorage.setItem(AUTH_TOKEN_KEY, token);
  localStorage.setItem(AUTH_EMAIL_KEY, email);
  localStorage.setItem(AUTH_NAME_KEY, name || "");
}

function clearSession() {
  localStorage.removeItem(AUTH_TOKEN_KEY);
  localStorage.removeItem(AUTH_EMAIL_KEY);
  localStorage.removeItem(AUTH_NAME_KEY);
}

function setUnauthorizedHandler(handler) {
  onUnauthorized = handler;
}

async function parseErrorMessage(response) {
  try {
    const body = await response.json();
    return body.message || `요청에 실패했습니다. (${response.status})`;
  } catch (e) {
    return `요청에 실패했습니다. (${response.status})`;
  }
}

async function request(url, options = {}) {
  const headers = { ...(options.headers || {}) };
  const token = getToken();
  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }

  const response = await fetch(url, { ...options, headers });

  if (response.status === 401) {
    clearSession();
    if (onUnauthorized) onUnauthorized();
    throw new Error("로그인이 필요합니다.");
  }

  if (!response.ok) {
    throw new Error(await parseErrorMessage(response));
  }

  return response;
}

const api = {
  async createInterest(url) {
    const response = await request(API_BASE, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ url }),
    });
    return response.json();
  },

  async getInterests() {
    const response = await request(API_BASE);
    return response.json();
  },

  async deleteInterest(id) {
    await request(`${API_BASE}/${id}`, { method: "DELETE" });
  },

  async getHistories(id) {
    const response = await request(`${API_BASE}/${id}/histories`);
    return response.json();
  },
};

const authApi = {
  async signup({ email, password, name }) {
    const response = await fetch(`${AUTH_BASE}/signup`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password, name }),
    });
    if (!response.ok) {
      throw new Error(await parseErrorMessage(response));
    }
    return response.json();
  },

  async login({ email, password }) {
    const response = await fetch(`${AUTH_BASE}/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password }),
    });
    if (!response.ok) {
      throw new Error(await parseErrorMessage(response));
    }
    return response.json();
  },
};

const auth = {
  getToken,
  getCurrentUser,
  setSession,
  clearSession,
  setUnauthorizedHandler,
};
