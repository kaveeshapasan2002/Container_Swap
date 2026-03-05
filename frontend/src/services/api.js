const API_BASE = '/api/containers';

export async function getStatus() {
  const res = await fetch(`${API_BASE}/status`);
  if (!res.ok) throw new Error('Failed to fetch status');
  return res.json();
}

export async function getContainer(id) {
  const res = await fetch(`${API_BASE}/${encodeURIComponent(id)}`);
  if (res.status === 404) return null;
  if (!res.ok) throw new Error('Failed to fetch container');
  return res.json();
}

export async function registerContainer(container) {
  const res = await fetch(API_BASE, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(container),
  });
  if (!res.ok) throw new Error('Failed to register container');
  return res.text();
}
