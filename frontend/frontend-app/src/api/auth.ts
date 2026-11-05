import api from "./client";

// tipovi
export type LoginResponse = {
  token: string;
};

export type UpdateProfilePayload = {
  firstName?: string;
  lastName?: string;
  dateOfBirth?: string; // ISO format (yyyy-mm-dd)
};

export type ChangePasswordPayload = {
  oldPassword: string;
  newPassword: string;
};

// funckije
export async function login(email: string, password: string) {
  const { data } = await api.post<LoginResponse>("/api/user/login", {
    email,
    password,
  });
  return data;
}

export async function register(payload: any) {
  const { data } = await api.post("/api/user/register", payload);
  return data;
}

// GET /user/me
export async function getMyProfile() {
  const { data } = await api.get("/api/user/me");
  return data;
}

// PUT /user/profile
export async function updateProfile(payload: UpdateProfilePayload) {
  const { data } = await api.put("/api/user/profile", payload);
  return data;
}

// PUT /user/password
export async function changePassword(payload: ChangePasswordPayload) {
  await api.put("/api/user/password", payload);
}
