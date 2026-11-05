<template>
  <!-- BACKGROUND -->
  <div
    class="min-h-screen relative flex items-start justify-center pt-24 overflow-hidden bg-gradient-to-br from-black via-slate-900 to-indigo-950"
  >
    <!-- STARS -->
    <div
      class="absolute inset-0 opacity-15 bg-[radial-gradient(white_1px,transparent_1px)] [background-size:40px_40px]"
    ></div>

    <!-- CONTENT -->
    <div
      class="relative z-10 w-full max-w-5xl p-10 rounded-3xl bg-black/60 backdrop-blur-xl border border-white/10 shadow-2xl"
    >
      <!-- TITLE -->
      <h1
        class="text-4xl font-extrabold text-center mb-10 flex items-center justify-center gap-3"
      >
        <!-- JOYSTICK -->
        <span class="text-4xl text-white">🎮</span>

        <!-- TEXT -->
        <span
          class="bg-clip-text text-transparent bg-gradient-to-r from-indigo-400 via-purple-400 to-pink-500"
        >
          Manage Games
        </span>
      </h1>

      <!-- CREATE GAME -->
      <div class="mb-10 p-6 rounded-2xl border border-white/10 bg-black/40">
        <h2 class="text-xl font-semibold text-indigo-300 mb-4">
          Create new game
        </h2>

        <div class="grid grid-cols-3 gap-4">
          <input
            v-model="newGame.name"
            placeholder="Name"
            class="px-4 py-2 rounded-lg bg-black/70 border border-white/10 text-white"
          />
          <input
            v-model="newGame.genre"
            placeholder="Genre"
            class="px-4 py-2 rounded-lg bg-black/70 border border-white/10 text-white"
          />
          <input
            v-model="newGame.description"
            placeholder="Description"
            class="px-4 py-2 rounded-lg bg-black/70 border border-white/10 text-white"
          />
        </div>

        <button
          @click="createGame"
          class="mt-4 px-6 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-500 transition text-white font-semibold"
        >
          Create
        </button>
      </div>

      <!-- GAMES LIST -->
      <div class="space-y-4">
        <div
          v-for="game in games"
          :key="game.id"
          class="p-5 rounded-xl border border-white/10 bg-black/40 flex justify-between items-center"
        >
          <div>
            <h3 class="text-lg font-semibold text-white">
              {{ game.name }}
            </h3>
            <p class="text-sm text-gray-400">
              {{ game.genre }} — {{ game.description }}
            </p>
          </div>

          <div class="flex gap-3">
            <button
              @click="startEdit(game)"
              class="px-4 py-1 rounded-lg border border-indigo-400 text-indigo-300 hover:bg-indigo-500 hover:text-white transition"
            >
              Edit
            </button>

            <button
              @click="deleteGame(game.id)"
              class="px-4 py-1 rounded-lg border border-red-400 text-red-400 hover:bg-red-500 hover:text-white transition"
            >
              Delete
            </button>
          </div>
        </div>
      </div>

      <!-- EDIT MODAL -->
      <div
        v-if="editingGame"
        class="fixed inset-0 bg-black/70 flex items-center justify-center z-50"
      >
        <div
          class="w-full max-w-md p-8 rounded-2xl bg-black/80 border border-white/10"
        >
          <h2 class="text-xl font-semibold text-indigo-300 mb-4">Edit game</h2>

          <input
            v-model="editingGame.name"
            placeholder="Name"
            class="w-full mb-3 px-4 py-2 rounded-lg bg-black/70 border border-white/10 text-white"
          />
          <input
            v-model="editingGame.genre"
            placeholder="Genre"
            class="w-full mb-3 px-4 py-2 rounded-lg bg-black/70 border border-white/10 text-white"
          />
          <input
            v-model="editingGame.description"
            placeholder="Description"
            class="w-full mb-6 px-4 py-2 rounded-lg bg-black/70 border border-white/10 text-white"
          />

          <div class="flex justify-end gap-4">
            <button
              @click="editingGame = null"
              class="px-4 py-2 rounded-lg text-gray-400 hover:text-white"
            >
              Cancel
            </button>
            <button
              @click="updateGame"
              class="px-4 py-2 rounded-lg bg-indigo-600 hover:bg-indigo-500 text-white"
            >
              Save
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import {
  getAllGames,
  createGame as createGameApi,
  updateGame as updateGameApi,
  deleteGame as deleteGameApi,
} from "@/api/session";

type Game = {
  id: number;
  name: string;
  genre: string;
  description?: string;
};

const games = ref<Game[]>([]);
const editingGame = ref<Game | null>(null);

const newGame = ref({
  name: "",
  genre: "",
  description: "",
});

async function loadGames() {
  games.value = await getAllGames();
}

async function createGame() {
  if (!newGame.value.name || !newGame.value.genre) return;

  await createGameApi(newGame.value);
  newGame.value = { name: "", genre: "", description: "" };
  await loadGames();
}

function startEdit(game: Game) {
  editingGame.value = { ...game };
}

async function updateGame() {
  if (!editingGame.value) return;

  await updateGameApi(editingGame.value.id, editingGame.value);
  editingGame.value = null;
  await loadGames();
}

async function deleteGame(id: number) {
  await deleteGameApi(id);
  await loadGames();
}

onMounted(loadGames);
</script>
