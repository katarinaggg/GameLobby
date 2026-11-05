<template>
  <div class="fixed inset-0 bg-black/70 flex items-center justify-center z-50">
    <div
      class="bg-black/80 border border-white/10 rounded-2xl p-8 w-full max-w-md"
    >
      <h2 class="text-xl font-bold text-white mb-6">Change password</h2>

      <Input label="Old password" type="password" v-model="oldPass" />
      <Input label="New password" type="password" v-model="newPass" />
      <Input label="Confirm password" type="password" v-model="confirmPass" />

      <p v-if="error" class="text-red-400 mt-2">{{ error }}</p>

      <div class="flex gap-4 mt-6">
        <button
          @click="submit"
          class="px-6 py-3 rounded-xl bg-green-600 hover:bg-green-500"
        >
          Save
        </button>
        <button
          @click="$emit('close')"
          class="px-6 py-3 rounded-xl bg-gray-600 hover:bg-gray-500"
        >
          Cancel
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { changePassword } from "@/api/auth";
import Input from "@/components/Input.vue";

const oldPass = ref("");
const newPass = ref("");
const confirmPass = ref("");
const error = ref("");

async function submit() {
  if (newPass.value !== confirmPass.value) {
    error.value = "Passwords do not match";
    return;
  }
  await changePassword({
    oldPassword: oldPass.value,
    newPassword: newPass.value,
  });
  error.value = "";
  oldPass.value = newPass.value = confirmPass.value = "";
  emit("close");
}

const emit = defineEmits(["close"]);
</script>
