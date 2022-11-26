import { apiCall } from ".";

export async function getPrincipal({ token }) {
  const res = await apiCall({
    method: "GET",
    path: "user/principal",
    token,
  });

  return res;
}

export async function getAll() {
  const res = await apiCall({
    method: "POST",
    path: "user/all",
  });
  return res;
}

export async function getAllExceptPrincipal({ email }) {
  const res = await apiCall({
    method: "POST",
    path: `user/all`,
    body: {
      principalEmail: email,
    },
  });
  return res;
}

export async function signIn({ body }) {
  const res = await apiCall({
    method: "POST",
    path: "auth/signin",
    body,
  });

  return res;
}
