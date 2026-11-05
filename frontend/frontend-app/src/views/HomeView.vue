<template>
  <!-- BACKGROUND -->
  <div
    class="min-h-screen relative flex items-center justify-center overflow-hidden bg-gradient-to-br from-black via-slate-900 to-indigo-950"
  >
    <!-- STARS LAYER 1 -->
    <div
      class="absolute inset-0 opacity-15 bg-[radial-gradient(white_1px,transparent_1px)] [background-size:40px_40px]"
    ></div>

    <!-- STARS LAYER 2 -->
    <div
      class="absolute inset-0 opacity-10 bg-[radial-gradient(white_1px,transparent_1px)] [background-size:80px_80px]"
    ></div>

    <!-- CONTENT CARD -->
    <div
      class="relative z-10 text-center max-w-xl p-12 rounded-3xl bg-black/60 backdrop-blur-xl border border-white/10 shadow-2xl"
    >
      <!-- TITLE -->
      <h1
        class="flex items-baseline justify-center gap-4 mb-6 text-5xl font-extrabold"
      >
        <span
          class="text-6xl -translate-y-1 drop-shadow-[0_0_10px_rgba(255,255,255,0.25)]"
        >
          🎮
        </span>

        <span
          class="bg-clip-text text-transparent bg-gradient-to-r from-indigo-400 via-purple-400 to-pink-500"
        >
          GameLobby
        </span>
      </h1>

      <!-- TAGLINE -->
      <p class="text-gray-300 text-lg mb-10">
        Organize, join and manage gaming sessions in real time.
      </p>

      <!-- BUTTONS -->
      <div class="flex justify-center gap-6">
        <RouterLink
          to="/login"
          class="px-8 py-3 rounded-xl font-semibold text-white bg-indigo-600 hover:bg-indigo-500 transition transform hover:scale-105 shadow-[0_0_20px_rgba(99,102,241,0.6)]"
        >
          Login
        </RouterLink>

        <RouterLink
          to="/register"
          class="px-8 py-3 rounded-xl font-semibold border border-indigo-500 text-indigo-400 hover:bg-indigo-500 hover:text-white transition transform hover:scale-105 shadow-[0_0_20px_rgba(99,102,241,0.4)]"
        >
          Register
        </RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { RouterLink } from "vue-router";
</script>
