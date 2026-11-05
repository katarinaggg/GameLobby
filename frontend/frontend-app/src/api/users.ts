import api from "./client";

export type AdminUser = {
  id: number;
  username: string;
  email: string;
  role: "ADMIN" | "PLAYER";
  blocked: boolean;
  active: boolean;
};

// GET /user  (ADMIN)
export async function getAllUsers(): Promise<AdminUser[]> {
  const { data } = await api.get("/api/user");
  return data;
}

// PUT /user/{id}/block
export async function blockUser(id: number): Promise<void> {
  await api.put(`/api/user/${id}/block`);
}

// PUT /user/{id}/unblock
export async function unblockUser(id: number): Promise<void> {
  await api.put(`/api/user/${id}/unblock`);
}
