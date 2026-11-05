<template>
  <div
    class="min-h-screen bg-gradient-to-br from-black via-slate-900 to-indigo-950"
  >
    <!-- NAVBAR -->
    <Navbar />

    <!-- PAGE CONTENT -->
    <main class="p-8">
      <RouterView />
    </main>
  </div>
</template>

<script setup lang="ts">
import { watch } from "vue";
import { useRouter } from "vue-router";
import Navbar from "@/components/NavBar.vue";
import { useAuthStore } from "@/stores/auth";
import { getMyProfile } from "@/api/auth";

const auth = useAuthStore();
const router = useRouter();

watch(
  () => auth.token,
  async (token) => {
    if (!token) return;

    // ne radi /me na login strani
    if (router.currentRoute.value.path === "/login") return;

    try {
      const me = await getMyProfile();
      auth.setUser(me);
    } catch {
      auth.logout();
      router.push("/login");
    }
  },
  { immediate: true }
);
</script>
