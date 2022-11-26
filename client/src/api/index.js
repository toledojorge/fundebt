export async function apiCall({ method, path, body, token }) {
  let error;
  let status;
  const res = await fetch(`/api/${path}`, {
    method,
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: body ? JSON.stringify(body) : null,
  }).catch((e) => (error = e));

  status = res.status;

  let data =
    (await res.json().catch((_e) => {
      return;
    })) || null;

  if ([500, 403, 401].indexOf(status) > -1) {
    error =
      data && data.message
        ? data.message
        : `${path} request failed with status ${status}`;
  }

  return {
    data: data ? data : null,
    error,
    status,
  };
}
