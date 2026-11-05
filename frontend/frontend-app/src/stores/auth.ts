import { defineStore } from "pinia";

export const useAuthStore = defineStore("auth", {
  state: () => {
    const token = localStorage.getItem("token");
    const rawUser = localStorage.getItem("user");

    let user = null;
    try {
      user = rawUser ? JSON.parse(rawUser) : null;
    } catch {
      user = null;
      localStorage.removeItem("user");
    }

    return {
      token,
      user,
    };
  },

  getters: {
    isAuthenticated: (state) => !!state.token,
    isAdmin: (state) => state.user?.role === "ADMIN",
    userId: (state) => state.user?.id,
  },

  actions: {
    setToken(token: string) {
      this.token = token;
      localStorage.setItem("token", token);
    },

    setUser(user: any) {
      this.user = user;
      localStorage.setItem("user", JSON.stringify(user));
    },

    logout() {
      this.token = null;
      this.user = null;

      localStorage.removeItem("token");
      localStorage.removeItem("user");
    },
  },
});
