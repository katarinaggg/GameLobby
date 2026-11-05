<template>
  <div
    class="min-h-screen relative flex items-start justify-center pt-24 overflow-hidden bg-gradient-to-br from-black via-slate-900 to-indigo-950"
  >
    <!-- STARS -->
    <div
      class="absolute inset-0 opacity-15 bg-[radial-gradient(white_1px,transparent_1px)] [background-size:40px_40px]"
    ></div>

    <!-- CARD -->
    <div
      class="relative z-10 w-full max-w-2xl p-10 rounded-3xl bg-black/60 backdrop-blur-xl border border-white/10 shadow-2xl"
    >
      <!-- TITLE -->
      <h1
        class="text-3xl font-extrabold text-center mb-8 flex items-center justify-center gap-2"
      >
        <span class="text-3xl">➕</span>
        <span
          class="bg-clip-text text-transparent bg-gradient-to-r from-indigo-400 via-purple-400 to-pink-500"
        >
          Create Session
        </span>
      </h1>

      <!-- FORM -->
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <!-- NAME -->
        <div class="flex flex-col gap-1">
          <label class="text-xs text-gray-400">Session name *</label>
          <input
            v-model="form.name"
            placeholder="e.g. Evening CS2 match"
            class="px-4 py-2 rounded-lg bg-black/70 border border-white/10 text-white"
          />
        </div>

        <!-- GAME -->
        <div class="flex flex-col gap-1">
          <label class="text-xs text-gray-400">Game *</label>
          <select
            v-model="form.gameId"
            class="px-4 py-2 rounded-lg bg-black/70 border border-white/10 text-white"
          >
            <option disabled value="">Select game</option>
            <option v-for="g in games" :key="g.id" :value="g.id">
              {{ g.name }}
            </option>
          </select>
        </div>

        <!-- MAX PLAYERS -->
        <div class="flex flex-col gap-1">
          <label class="text-xs text-gray-400">Maximum players *</label>
          <input
            v-model.number="form.maxPlayers"
            type="number"
            min="1"
            class="px-4 py-2 rounded-lg bg-black/70 border border-white/10 text-white"
          />
        </div>

        <!-- TYPE -->
        <div class="flex flex-col gap-1">
          <label class="text-xs text-gray-400">Session type *</label>
          <select
            v-model="form.sessionType"
            class="px-4 py-2 rounded-lg bg-black/70 border border-white/10 text-white"
          >
            <option value="PUBLIC">Public (anyone can join)</option>
            <option value="PRIVATE">Private (invite only)</option>
          </select>
        </div>

        <!-- START TIME -->
        <div class="flex flex-col gap-1 md:col-span-2">
          <label class="text-xs text-gray-400">Start date & time *</label>
          <input
            v-model="form.startTime"
            type="datetime-local"
            class="px-4 py-2 rounded-lg bg-black/70 border border-white/10 text-white"
          />
        </div>

        <!-- DESCRIPTION -->
        <div class="flex flex-col gap-1 md:col-span-2">
          <label class="text-xs text-gray-400">Description</label>
          <textarea
            v-model="form.description"
            rows="3"
            placeholder="Optional additional info about the session"
            class="px-4 py-2 rounded-lg bg-black/70 border border-white/10 text-white"
          ></textarea>
        </div>
      </div>

      <!-- SUBMIT -->
      <button
        @click="submit"
        class="w-full mt-6 px-4 py-3 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-semibold transition"
      >
        Create session
      </button>

      <!-- ERROR -->
      <p v-if="error" class="text-red-400 text-center mt-3">
        {{ error }}
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { getAllGames, createSession } from "@/api/session";

const router = useRouter();
const games = ref<any[]>([]);
const error = ref("");

const form = ref({
  name: "",
  gameId: "" as number | "",
  maxPlayers: 1,
  sessionType: "PUBLIC" as "PUBLIC" | "PRIVATE",
  startTime: "",
  description: "",
});

onMounted(async () => {
  games.value = await getAllGames();
});

async function submit() {
  error.value = "";

  if (!form.value.name || !form.value.gameId || !form.value.startTime) {
    error.value = "Please fill all required fields";
    return;
  }

  try {
    await createSession({
      name: form.value.name,
      gameId: Number(form.value.gameId),
      maxPlayers: form.value.maxPlayers,
      sessionType: form.value.sessionType,
      startTime: form.value.startTime,
      description: form.value.description,
    });

    router.push("/sessions");
  } catch (e: any) {
    error.value = e?.response?.data?.message || "Failed to create session";
  }
}
</script>
