<template>
  <!-- BACKGROUND -->
  <div
    class="min-h-screen relative flex items-center justify-center overflow-hidden bg-gradient-to-br from-black via-slate-900 to-indigo-950"
  >
    <!-- STARS -->
    <div
      class="absolute inset-0 opacity-15 bg-[radial-gradient(white_1px,transparent_1px)] [background-size:40px_40px]"
    ></div>
    <div
      class="absolute inset-0 opacity-10 bg-[radial-gradient(white_1px,transparent_1px)] [background-size:80px_80px]"
    ></div>

    <!-- CARD -->
    <div
      class="relative z-10 w-full max-w-md p-10 rounded-3xl bg-black/60 backdrop-blur-xl border border-white/10 shadow-2xl"
    >
      <!-- TITLE -->
      <h1 class="text-3xl font-extrabold text-center mb-8">
        <span
          class="bg-clip-text text-transparent bg-gradient-to-r from-indigo-400 via-purple-400 to-pink-500"
        >
          Login
        </span>
      </h1>

      <!-- FORM -->
      <form class="space-y-6" @submit.prevent="submit">
        <!-- EMAIL -->
        <div>
          <label class="block text-gray-300 mb-1">Email</label>
          <input
            type="email"
            v-model="email"
            placeholder="you@example.com"
            autocomplete="email"
            class="w-full px-4 py-3 rounded-xl bg-gray-900 border border-gray-700 text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-indigo-500"
          />
        </div>

        <!-- PASSWORD -->
        <div>
          <label class="block text-gray-300 mb-1">Password</label>
          <input
            type="password"
            v-model="password"
            placeholder="••••••••"
            autocomplete="current-password"
            class="w-full px-4 py-3 rounded-xl bg-gray-900 border border-gray-700 text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-indigo-500"
          />
        </div>

        <!-- SUBMIT -->
        <button
          type="submit"
          class="w-full py-3 rounded-xl font-semibold text-white bg-indigo-600 hover:bg-indigo-500 transition shadow-[0_0_20px_rgba(99,102,241,0.6)]"
        >
          Login
        </button>
      </form>

      <!-- BACK TO HOME -->
      <RouterLink
        to="/"
        class="mt-6 flex justify-center items-center gap-2 text-indigo-400 hover:text-indigo-300 transition"
      >
        <span class="text-lg">←</span>
        <span>Back to Home</span>
      </RouterLink>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useRouter, RouterLink } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import { login } from "@/api/auth";

const auth = useAuthStore();

const router = useRouter();

const email = ref("");
const password = ref("");

const loading = ref(false);
const error = ref<string | null>(null);

async function submit() {
  error.value = null;
  loading.value = true;

  try {
    const res = await login(email.value, password.value);

    console.log("✅ Login successful");

    auth.setToken(res.token);
    router.push("/profile");
  } catch (e: any) {
    error.value = e?.response?.data?.message || "Invalid email or password.";
  } finally {
    loading.value = false;
  }
}
</script>
