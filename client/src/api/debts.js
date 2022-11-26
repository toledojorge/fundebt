import { apiCall } from ".";

export async function create({ body, token }) {
  const data = await apiCall({
    method: "POST",
    path: `debt/create`,
    token,
    body,
  });
  return data;
}

export async function getPaginated({ debtUserId, filters, token }) {
  const data = await apiCall({
    method: "POST",
    path: "debt/paginated",
    body: {
      debtUserId,
      filters,
    },
    token,
  });
  return data;
}

export async function getBalance({ debtUserId, token }) {
  const { data } = await apiCall({
    method: "POST",
    path: `debt/balance`,
    body: {
      debtUserId,
    },
    token,
  });
  return data ? data : 0;
}

export async function getSumByMonth({ debtUserId, token }) {
  const { data } = await apiCall({
    method: "POST",
    path: `debt/sum/byMonth`,
    body: {
      debtUserId,
    },
    token,
  });

  return data;
}

export async function markAsPaid({ ids, token }) {
  await apiCall({
    method: "POST",
    path: `debt/paid`,
    body: ids,
    token,
  });
}
