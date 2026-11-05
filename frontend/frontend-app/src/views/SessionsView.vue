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
      class="relative z-10 w-full max-w-6xl p-10 rounded-3xl bg-black/60 backdrop-blur-xl border border-white/10 shadow-2xl"
    >
      <!-- TITLE -->
      <h1
        class="text-4xl font-extrabold text-center mb-10 flex items-center justify-center gap-3"
      >
        <span class="text-4xl">🎮</span>
        <span
          class="bg-clip-text text-transparent bg-gradient-to-r from-indigo-400 via-purple-400 to-pink-500"
        >
          Gaming Sessions
        </span>
      </h1>

      <!-- FILTERS + SORT -->
      <div class="mb-10 p-6 rounded-2xl border border-white/10 bg-black/40">
        <h2 class="text-lg font-semibold text-indigo-300 mb-6">Filters</h2>

        <!-- FILTER FIELDS -->
        <div class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
          <!-- GAME -->
          <div class="flex flex-col gap-1">
            <label class="text-xs text-gray-400">Game</label>
            <select
              v-model="filters.gameId"
              class="px-3 py-2 rounded-lg bg-black/70 border border-white/10 text-white"
            >
              <option :value="null">All games</option>
              <option v-for="g in games" :key="g.id" :value="g.id">
                {{ g.name }}
              </option>
            </select>
          </div>

          <!-- TYPE -->
          <div class="flex flex-col gap-1">
            <label class="text-xs text-gray-400">Session type</label>
            <select
              v-model="filters.type"
              class="px-3 py-2 rounded-lg bg-black/70 border border-white/10 text-white"
            >
              <option :value="null">All types</option>
              <option value="PUBLIC">Public</option>
              <option value="PRIVATE">Private</option>
            </select>
          </div>

          <!-- MAX PLAYERS -->
          <div class="flex flex-col gap-1">
            <label class="text-xs text-gray-400">Max players</label>
            <input
              v-model.number="filters.maxPlayers"
              type="number"
              min="1"
              placeholder="Any"
              class="px-3 py-2 rounded-lg bg-black/70 border border-white/10 text-white"
            />
          </div>

          <!-- DESCRIPTION -->
          <div class="flex flex-col gap-1">
            <label class="text-xs text-gray-400">Description contains</label>
            <input
              v-model="filters.description"
              placeholder="Search text"
              class="px-3 py-2 rounded-lg bg-black/70 border border-white/10 text-white"
            />
          </div>
        </div>

        <!-- SORT -->
        <h3 class="text-sm font-semibold text-indigo-200 mb-2">Sort</h3>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-4 items-end">
          <!-- SORT BY -->
          <div class="flex flex-col gap-1">
            <select
              v-model="filters.sortBy"
              class="px-3 py-2 rounded-lg bg-black/70 border border-white/10 text-white"
            >
              <option :value="null">None</option>
              <option value="startTime">Start time</option>
              <option value="participantsCount">Participants</option>
            </select>
          </div>

          <!-- ONLY MINE -->
          <label class="flex items-center gap-2 text-gray-300 mt-5">
            <input type="checkbox" v-model="filters.onlyMine" />
            Joined sessions only
          </label>

          <!-- ACTIONS -->
          <div class="flex gap-4 justify-end">
            <button
              @click="applyFilters"
              class="px-5 py-2 rounded-lg bg-indigo-600 hover:bg-indigo-500 text-white transition"
            >
              Apply
            </button>

            <button
              @click="resetFilters"
              class="px-5 py-2 rounded-lg border border-white/20 text-gray-300 hover:text-white transition"
            >
              Reset
            </button>
          </div>
        </div>
      </div>

      <!-- CREATE SESSION -->
      <div class="flex justify-end mb-10">
        <RouterLink
          to="/sessions/create"
          class="px-6 py-3 rounded-xl bg-gradient-to-r from-indigo-500 to-purple-600 text-white font-semibold shadow-lg hover:opacity-90 transition"
        >
          + Create new session
        </RouterLink>
      </div>

      <!-- SESSIONS LIST -->
      <div class="space-y-6">
        <div
          v-for="s in sessions"
          :key="s.sessionId"
          class="p-6 rounded-2xl border border-white/10 bg-black/40"
        >
          <div class="grid grid-cols-1 md:grid-cols-4 gap-6">
            <!-- INFO -->
            <div class="md:col-span-3 space-y-2">
              <h3 class="text-2xl font-semibold text-white">
                {{ s.name }}
              </h3>

              <p class="text-sm text-gray-400">
                🎮 {{ s.gameName }} • {{ s.sessionType }}
              </p>

              <p class="text-sm text-gray-400">
                🕒 {{ formatDate(s.startTime) }}
              </p>

              <p class="text-gray-300">
                {{ s.description || "No description provided." }}
              </p>

              <div class="flex gap-6 text-sm mt-2">
                <span class="text-indigo-300">
                  👥 {{ s.participantIds.length }} / {{ s.maxPlayers }}
                </span>

                <span class="text-gray-300">
                  Status:
                  <span :class="statusClass(s.status)">
                    {{ s.status }}
                  </span>
                </span>
              </div>
            </div>

            <!-- ACTIONS -->
            <div class="flex flex-col gap-2">
              <button
                v-if="canJoin(s)"
                @click="join(s.sessionId)"
                class="px-4 py-2 rounded-lg bg-indigo-600 hover:bg-indigo-500 text-white transition"
              >
                Join
              </button>

              <button
                v-if="canLeave(s)"
                @click="leave(s.sessionId)"
                class="px-4 py-2 rounded-lg border border-white/20 text-gray-300 hover:text-white transition"
              >
                Leave
              </button>

              <button
                v-if="canInvite(s)"
                @click="invite(s.sessionId)"
                class="px-4 py-2 rounded-lg border border-white/20 text-gray-300 hover:text-white transition"
              >
                Invite
              </button>

              <button
                v-if="canCancel(s)"
                @click="cancel(s.sessionId)"
                class="px-4 py-2 rounded-lg border border-red-400 text-red-400 hover:bg-red-500 hover:text-white transition"
              >
                Cancel Session
              </button>

              <button
                v-if="canClose(s)"
                @click="openCloseModal(s)"
                class="px-4 py-2 rounded-lg bg-indigo-600 hover:bg-indigo-500 text-white transition"
              >
                Finish Session
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- CLOSE SESSION MODAL -->
    <div
      v-if="closingSession"
      class="fixed inset-0 z-50 bg-black/70 flex items-center justify-center"
    >
      <div
        class="bg-black/80 p-8 rounded-2xl w-full max-w-md border border-white/10"
      >
        <h2 class="text-lg text-indigo-300 mb-4">Finish session</h2>

        <div v-for="u in closingSession.participants" :key="u.id" class="mb-2">
          <label class="flex items-center gap-2 text-gray-300">
            <input type="checkbox" v-model="attendance[u.id]" />
            {{ u.username }}
          </label>
        </div>

        <div class="flex justify-end gap-4 mt-6">
          <button
            @click="closingSession = null"
            class="px-4 py-2 rounded-lg border border-white/20 text-gray-300 hover:text-white"
          >
            Cancel
          </button>

          <button
            @click="closeSession"
            class="px-4 py-2 rounded-lg bg-indigo-600 hover:bg-indigo-500 text-white"
          >
            Confirm
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { RouterLink } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import {
  getAllSessions,
  getFilteredSessions,
  joinSession,
  leaveSession,
  cancelSession,
  inviteUserToSession,
  closeSession as closeSessionApi,
  getAllGames,
} from "@/api/session";

const auth = useAuthStore();

const sessions = ref<any[]>([]);
const games = ref<any[]>([]);

const filters = ref<{
  gameId: number | null;
  type: "PUBLIC" | "PRIVATE" | null;
  maxPlayers: number | null;
  description: string;
  sortBy: "startTime" | "participantsCount" | null;
  onlyMine: boolean;
}>({
  gameId: null,
  type: null,
  maxPlayers: null,
  description: "",
  sortBy: null,
  onlyMine: false,
});

const closingSession = ref<any | null>(null);
const attendance = ref<Record<number, boolean>>({});

function formatDate(date: string) {
  return new Date(date).toLocaleString();
}

function statusClass(status: string) {
  return {
    SCHEDULED: "text-yellow-400",
    CANCELED: "text-red-400",
    CLOSED: "text-green-400",
  }[status];
}

// permissions
const canJoin = (s: any) =>
  s.status === "SCHEDULED" &&
  s.sessionType === "PUBLIC" &&
  !s.participantIds.includes(auth.userId) &&
  s.organizerId !== auth.userId;

const canLeave = (s: any) =>
  s.status === "SCHEDULED" &&
  s.participantIds.includes(auth.userId) &&
  s.organizerId !== auth.userId;

const canInvite = (s: any) =>
  s.status === "SCHEDULED" &&
  s.sessionType === "PRIVATE" &&
  s.organizerId === auth.userId;

const canCancel = (s: any) =>
  s.status === "SCHEDULED" && (s.organizerId === auth.userId || auth.isAdmin);

const canClose = canCancel;

// actions
async function loadSessions() {
  sessions.value = await getAllSessions();
}

async function applyFilters() {
  sessions.value = await getFilteredSessions({
    gameId: filters.value.gameId ?? undefined,
    type: filters.value.type ?? undefined,
    maxPlayers: filters.value.maxPlayers ?? undefined,
    description: filters.value.description || undefined,
    userId: filters.value.onlyMine ? auth.userId : undefined,
    sortBy: filters.value.sortBy ?? undefined,
  });
}

function resetFilters() {
  filters.value = {
    gameId: null,
    type: null,
    maxPlayers: null,
    description: "",
    sortBy: null,
    onlyMine: false,
  };
  loadSessions();
}

async function join(id: number) {
  await joinSession(id);
  loadSessions();
}

async function leave(id: number) {
  await leaveSession(id);
  loadSessions();
}

async function cancel(id: number) {
  await cancelSession(id);
  loadSessions();
}

async function invite(sessionId: number) {
  const invitedUserId = Number(prompt("Enter user ID to invite"));
  if (invitedUserId) {
    await inviteUserToSession(sessionId, invitedUserId);
  }
}

function openCloseModal(s: any) {
  closingSession.value = s;
  attendance.value = {};
  s.participants.forEach((u: any) => (attendance.value[u.id] = false));
}

async function closeSession() {
  await closeSessionApi({
    sessionId: closingSession.value.sessionId,
    participantsAttendance: attendance.value,
  });
  closingSession.value = null;
  loadSessions();
}

onMounted(async () => {
  games.value = await getAllGames();
  loadSessions();
});
</script>
