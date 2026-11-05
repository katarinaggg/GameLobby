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
      class="relative z-10 w-full max-w-2xl p-12 rounded-3xl bg-black/60 backdrop-blur-xl border border-white/10 shadow-2xl"
    >
      <!-- TITLE -->
      <h1 class="text-3xl font-extrabold text-center mb-8">
        <span
          class="bg-clip-text text-transparent bg-gradient-to-r from-indigo-400 via-purple-400 to-pink-500"
        >
          Register
        </span>
      </h1>

      <form @submit.prevent="submit">
        <!-- GRID -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-7">
          <!-- LEFT COLUMN -->
          <div class="space-y-5">
            <!-- USERNAME -->
            <div>
              <label class="block text-gray-300 mb-1">Username</label>
              <input
                type="text"
                v-model="username"
                placeholder="username"
                class="w-full px-4 py-3 rounded-xl bg-gray-900 border border-gray-700 text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-indigo-500"
              />
            </div>

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
                autocomplete="new-password"
                class="w-full px-4 py-3 rounded-xl bg-gray-900 border border-gray-700 text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-indigo-500"
              />
            </div>
          </div>

          <!-- RIGHT COLUMN -->
          <div class="space-y-5">
            <!-- FIRST NAME -->
            <div>
              <label class="block text-gray-300 mb-1">First name</label>
              <input
                type="text"
                v-model="firstName"
                placeholder="First name"
                class="w-full px-4 py-3 rounded-xl bg-gray-900 border border-gray-700 text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-indigo-500"
              />
            </div>

            <!-- LAST NAME -->
            <div>
              <label class="block text-gray-300 mb-1">Last name</label>
              <input
                type="text"
                v-model="lastName"
                placeholder="Last name"
                class="w-full px-4 py-3 rounded-xl bg-gray-900 border border-gray-700 text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-indigo-500"
              />
            </div>

            <!-- DATE OF BIRTH -->
            <div>
              <label class="block text-gray-300 mb-1">Date of birth</label>
              <input
                type="date"
                v-model="dateOfBirth"
                class="w-full px-4 py-3 rounded-xl bg-gray-900 border border-gray-700 text-white focus:outline-none focus:ring-2 focus:ring-indigo-500"
              />
            </div>
          </div>
        </div>

        <!-- SUBMIT -->
        <button
          type="submit"
          class="w-full py-3 mt-8 rounded-xl font-semibold text-white bg-indigo-600 hover:bg-indigo-500 transition shadow-[0_0_20px_rgba(99,102,241,0.6)]"
        >
          Register
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
import { useRouter } from "vue-router";
import { register } from "@/api/auth";

const router = useRouter();

const username = ref("");
const email = ref("");
const firstName = ref("");
const lastName = ref("");
const dateOfBirth = ref("");
const password = ref("");

const loading = ref(false);
const error = ref<string | null>(null);

async function submit() {
  error.value = null;
  loading.value = true;

  try {
    await register({
      username: username.value,
      email: email.value,
      firstName: firstName.value,
      lastName: lastName.value,
      dateOfBirth: dateOfBirth.value,
      password: password.value,
    });

    console.log("✅ Registration successful for:", email.value);

    router.push("/login");
  } catch (e: any) {
    error.value =
      e?.response?.data?.message || "Registration failed. Try again.";
  } finally {
    loading.value = false;
  }
}
</script>
