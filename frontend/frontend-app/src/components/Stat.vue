<template>
  <div class="rounded-2xl bg-white/5 border border-white/10 p-6 text-center">
    <p class="text-sm text-gray-400 mb-1">
      {{ label }}
    </p>
    <p class="text-2xl font-bold text-white">
      {{ value ?? "-" }}
    </p>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  label: string;
  value?: string | number;
}>();
</script>
