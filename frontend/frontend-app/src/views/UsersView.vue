<template>
  <div
    class="min-h-screen pt-24 px-6 bg-gradient-to-br from-black via-slate-900 to-indigo-950"
  >
    <div
      class="max-w-5xl mx-auto bg-black/60 border border-white/10 rounded-3xl p-8"
    >
      <h1 class="text-2xl font-bold text-white mb-6">Users</h1>

      <table class="w-full text-gray-200">
        <thead class="text-gray-400">
          <tr>
            <th class="text-left py-2">Username</th>
            <th>Email</th>
            <th>Status</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="u in users" :key="u.id" class="border-t border-white/10">
            <td class="py-3">{{ u.username }}</td>
            <td>{{ u.email }}</td>
            <td>
              <span :class="u.blocked ? 'text-red-400' : 'text-green-400'">
                {{ u.blocked ? "Blocked" : "Active" }}
              </span>
            </td>
            <td class="text-right">
              <button
                @click="toggle(u)"
                class="px-4 py-2 rounded-lg"
                :class="u.blocked ? 'bg-green-600' : 'bg-red-600'"
              >
                {{ u.blocked ? "Unblock" : "Block" }}
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { getAllUsers, blockUser, unblockUser } from "@/api/users";

const users = ref<any[]>([]);

onMounted(async () => {
  users.value = await getAllUsers();
});

async function toggle(u: any) {
  if (u.blocked) {
    await unblockUser(u.id);
    u.blocked = false;
  } else {
    await blockUser(u.id);
    u.blocked = true;
  }
}
</script>
