<template>
  <div class="flex flex-col gap-1">
    <label class="text-sm text-gray-400">
      {{ label }}
    </label>

    <input
      :type="type"
      :value="modelValue"
      @input="
        $emit('update:modelValue', ($event.target as HTMLInputElement).value)
      "
      :disabled="disabled"
      class="px-4 py-3 rounded-xl bg-black/50 border border-white/10 text-white focus:outline-none focus:border-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed"
    />
  </div>
</template>

<script setup lang="ts">
defineProps<{
  label: string;
  modelValue?: string;
  type?: string;
  disabled?: boolean;
}>();

defineEmits(["update:modelValue"]);
</script>
