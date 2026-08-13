const API_BASE = "/api/interests";

async function parseErrorMessage(response) {
  try {
    const body = await response.json();
    return body.message || `요청에 실패했습니다. (${response.status})`;
  } catch (e) {
    return `요청에 실패했습니다. (${response.status})`;
  }
}

const api = {
  async createInterest(url) {
    const response = await fetch(API_BASE, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ url }),
    });
    if (!response.ok) {
      throw new Error(await parseErrorMessage(response));
    }
    return response.json();
  },

  async getInterests() {
    const response = await fetch(API_BASE);
    if (!response.ok) {
      throw new Error(await parseErrorMessage(response));
    }
    return response.json();
  },

  async deleteInterest(id) {
    const response = await fetch(`${API_BASE}/${id}`, { method: "DELETE" });
    if (!response.ok) {
      throw new Error(await parseErrorMessage(response));
    }
  },

  async getHistories(id) {
    const response = await fetch(`${API_BASE}/${id}/histories`);
    if (!response.ok) {
      throw new Error(await parseErrorMessage(response));
    }
    return response.json();
  },
};
