<template>
  <nav
    class="w-full px-12 py-5 grid grid-cols-3 items-center bg-black/60 backdrop-blur-xl border-b border-white/10"
  >
    <!-- LEFT: LOGO -->
    <RouterLink
      to="/profile"
      class="justify-self-start flex items-center gap-4"
    >
      <span class="text-3xl"> 🎮 </span>

      <span
        class="text-2xl font-extrabold bg-clip-text text-transparent bg-gradient-to-r from-indigo-400 via-purple-400 to-pink-500"
      >
        GameLobby
      </span>
    </RouterLink>

    <!-- CENTER: NAV LINKS -->
    <div class="justify-self-center flex gap-10 text-lg text-gray-200">
      <RouterLink to="/profile" class="hover:text-indigo-400 transition">
        My Profile
      </RouterLink>

      <RouterLink to="/sessions" class="hover:text-indigo-400 transition">
        Sessions
      </RouterLink>

      <RouterLink to="/notifications" class="hover:text-indigo-400 transition">
        Notifications
      </RouterLink>

      <!-- ADMIN ONLY -->
      <RouterLink
        v-if="auth.isAdmin"
        to="/users"
        class="hover:text-indigo-400 transition text-indigo-300"
      >
        Users
      </RouterLink>

      <RouterLink
        v-if="auth.isAdmin"
        to="/admin/games"
        class="hover:text-indigo-400 transition text-indigo-300"
      >
        Games
      </RouterLink>
    </div>

    <!-- RIGHT: LOGOUT -->
    <button
      @click="handleLogout"
      class="justify-self-end text-lg text-gray-400 hover:text-red-400 transition"
    >
      Logout
    </button>
  </nav>
</template>

<script setup lang="ts">
import { RouterLink } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import { useRouter } from "vue-router";

const auth = useAuthStore();
const router = useRouter();

function handleLogout() {
  auth.logout();
  router.push("/");
}
</script>
