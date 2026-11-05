<template>
  <div
    class="min-h-screen relative overflow-hidden pt-24 px-6 bg-gradient-to-br from-black via-slate-900 to-indigo-950"
  >
    <!-- DOTS / STARS BACKGROUND -->
    <div
      class="absolute inset-0 opacity-15 bg-[radial-gradient(white_1px,transparent_1px)] [background-size:40px_40px] pointer-events-none"
    ></div>

    <!-- CONTENT -->
    <div
      v-if="profile"
      class="relative z-10 max-w-5xl mx-auto bg-black/60 backdrop-blur-xl border border-white/10 rounded-3xl p-10 text-gray-200"
    >
      <!-- HEADER -->
      <div class="flex items-center gap-6 mb-8">
        <div
          class="w-20 h-20 rounded-full bg-indigo-600 flex items-center justify-center text-4xl"
        >
          👤
        </div>
        <div>
          <h1 class="text-3xl font-bold text-white">
            {{ profile.firstName }} {{ profile.lastName }}
          </h1>
          <p class="text-gray-400">@{{ profile.username }}</p>

          <span
            class="inline-block mt-2 px-3 py-1 rounded-full text-sm font-semibold"
            :class="profile.role === 'ADMIN' ? 'bg-red-600' : 'bg-indigo-600'"
          >
            {{ profile.role }}
          </span>
        </div>
      </div>

      <!-- TITLE BADGE (PLAYER ONLY) -->
      <div v-if="isPlayer && profile.organizerTitle" class="mb-8">
        <div
          class="inline-flex items-center gap-3 px-6 py-3 rounded-2xl text-xl font-bold shadow-lg"
          :class="titleClass(profile.organizerTitle)"
        >
          <span class="text-2xl">{{ titleIcon(profile.organizerTitle) }}</span>
          <span>{{ profile.organizerTitle }}</span>
        </div>
      </div>

      <!-- STATS (PLAYER ONLY) -->
      <div v-if="isPlayer" class="grid grid-cols-2 md:grid-cols-3 gap-6 mb-10">
        <Stat label="Applied" :value="profile.totalSessionsApplied" />
        <Stat label="Attended" :value="profile.totalSessionsAttended" />
        <Stat label="Missed" :value="profile.totalSessionsMissed" />
        <Stat label="Attendance %" :value="profile.attendancePercentage" />
        <Stat label="Organized" :value="profile.organizedSessionsCount" />
      </div>

      <!-- PROFILE FORM -->
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
        <Input
          label="First name"
          v-model="form.firstName"
          :disabled="!editing"
        />
        <Input label="Last name" v-model="form.lastName" :disabled="!editing" />
        <Input
          label="Date of birth"
          type="date"
          v-model="form.dateOfBirth"
          :disabled="!editing"
        />
        <Input label="Email" :modelValue="profile.email" disabled />
        <Input label="Username" :modelValue="profile.username" disabled />
      </div>

      <!-- ACTIONS -->
      <div class="flex gap-4">
        <button
          v-if="!editing"
          @click="editing = true"
          class="px-6 py-3 rounded-xl bg-indigo-600 hover:bg-indigo-500"
        >
          Edit profile
        </button>

        <button
          v-else
          @click="saveProfile"
          class="px-6 py-3 rounded-xl bg-green-600 hover:bg-green-500"
        >
          Save
        </button>

        <button
          v-if="editing"
          @click="cancelEdit"
          class="px-6 py-3 rounded-xl bg-gray-600 hover:bg-gray-500"
        >
          Cancel
        </button>

        <button
          @click="showPasswordModal = true"
          class="ml-auto px-6 py-3 rounded-xl bg-red-600 hover:bg-red-500"
        >
          Change password
        </button>
      </div>
    </div>

    <!-- CHANGE PASSWORD MODAL -->
    <ChangePasswordModal
      v-if="showPasswordModal"
      @close="showPasswordModal = false"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from "vue";
import { updateProfile } from "@/api/auth";
import { useAuthStore } from "@/stores/auth";
import Stat from "@/components/Stat.vue";
import Input from "@/components/Input.vue";
import ChangePasswordModal from "@/components/ChangePasswordModal.vue";

const auth = useAuthStore();

// user dolazi iz globalnog store-a
const profile = computed(() => auth.user);

const form = ref({
  firstName: "",
  lastName: "",
  dateOfBirth: "",
});

const editing = ref(false);
const showPasswordModal = ref(false);

const isPlayer = computed(() => profile.value?.role === "PLAYER");

// kad se user učita (iz AppLayout-a) → popuni formu
watch(
  profile,
  (u) => {
    if (!u) return;
    form.value = {
      firstName: u.firstName,
      lastName: u.lastName,
      dateOfBirth: u.dateOfBirth,
    };
  },
  { immediate: true }
);

function cancelEdit() {
  editing.value = false;
  if (!profile.value) return;

  form.value = {
    firstName: profile.value.firstName,
    lastName: profile.value.lastName,
    dateOfBirth: profile.value.dateOfBirth,
  };
}

async function saveProfile() {
  if (!profile.value) return;

  const payload: any = {};

  if (form.value.firstName !== profile.value.firstName) {
    payload.firstName = form.value.firstName;
  }
  if (form.value.lastName !== profile.value.lastName) {
    payload.lastName = form.value.lastName;
  }
  if (form.value.dateOfBirth !== profile.value.dateOfBirth) {
    payload.dateOfBirth = form.value.dateOfBirth;
  }

  if (Object.keys(payload).length > 0) {
    const updated = await updateProfile(payload);
    auth.setUser(updated); // osveži globalni user
  }

  editing.value = false;
}

function titleIcon(t: string) {
  return titleMeta[t]?.icon ?? "⚪";
}

function titleClass(t: string) {
  return titleMeta[t]?.class ?? "bg-gray-600";
}

const titleMeta: Record<string, { icon: string; class: string }> = {
  BARJAKTAR: { icon: "🟢", class: "bg-green-600" },
  HAJDUK: { icon: "🔵", class: "bg-blue-600" },
  VOJVODA: { icon: "🟣", class: "bg-purple-600" },
  KNEZ: { icon: "🟡", class: "bg-yellow-500 text-black" },
};
</script>
