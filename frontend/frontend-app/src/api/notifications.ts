import api from "./client";
import { useAuthStore } from "@/stores/auth";

export async function getNotifications() {
  const auth = useAuthStore();

  // ADMIN → sve notifikacije
  if (auth.user?.role === "ADMIN") {
    const res = await api.get("/api/notifications");
    return res.data;
  }

  // USER → samo svoje (po email-u)
  const res = await api.get("/api/notifications/user", {
    params: {
      email: auth.user?.email,
    },
  });

  return res.data;
}
