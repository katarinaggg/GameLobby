import axios from "axios";
import { useAuthStore } from "../stores/auth";

// povezivanje na api gateway
const api = axios.create({
  baseURL: "http://localhost:8084",
});

api.interceptors.request.use((config) => {
  const auth = useAuthStore();
  if (auth.token) {
    config.headers = config.headers || {};
    config.headers.Authorization = `Bearer ${auth.token}`;
  }
  return config;
});

export default api;
