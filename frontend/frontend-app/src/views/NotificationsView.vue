<template>
  <div
    class="min-h-screen pt-24 px-6 bg-gradient-to-br from-black via-slate-900 to-indigo-950"
  >
    <div
      class="max-w-4xl mx-auto bg-black/60 border border-white/10 rounded-3xl p-8"
    >
      <h1 class="text-2xl font-bold text-white mb-6">Notifications</h1>

      <div v-if="notifications.length === 0" class="text-gray-400">
        No notifications.
      </div>

      <div class="space-y-4">
        <div
          v-for="n in notifications"
          :key="n.id"
          class="p-5 rounded-xl bg-black/70 border border-white/10"
        >
          <!-- HEADER -->
          <div class="flex justify-between items-center mb-2">
            <h2
              class="text-lg font-semibold text-indigo-400 flex items-center gap-2"
            >
              <span>📬</span>
              <span>{{ n.subject }}</span>
            </h2>

            <span class="text-sm text-gray-400">
              {{ formatDate(n.createdAt) }}
            </span>
          </div>

          <!-- CONTENT -->
          <p class="text-gray-300 whitespace-pre-line">
            {{ n.content }}
          </p>

          <!-- ADMIN vidi kome je poslato -->
          <div v-if="isAdmin" class="mt-3 text-sm text-gray-400">
            To: {{ n.recipientEmail }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, computed } from "vue";
import { getNotifications } from "@/api/notifications";
import { useAuthStore } from "@/stores/auth";

const auth = useAuthStore();
const notifications = ref<any[]>([]);

const isAdmin = computed(() => auth.user?.role === "ADMIN");

onMounted(async () => {
  notifications.value = await getNotifications();
});

function formatDate(d: string): string {
  if (!d) return "";

  const date = new Date(d);

  if (isNaN(date.getTime())) return "";

  return date.toLocaleString("sr-RS", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
}
</script>
