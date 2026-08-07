<script setup lang="ts">
import Modal from './Modal.vue'

withDefaults(
  defineProps<{
    open: boolean
    title?: string
    message: string
    confirmLabel?: string
    cancelLabel?: string
    busy?: boolean
  }>(),
  {
    title: 'Confirm action',
    confirmLabel: 'Delete',
    cancelLabel: 'Cancel',
    busy: false,
  },
)

const emit = defineEmits<{ close: []; confirm: [] }>()
</script>

<template>
  <Modal :open="open" :title="title" @close="emit('close')">
    <p class="text-sm text-gray-600">{{ message }}</p>
    <slot />
    <div class="mt-6 flex justify-end gap-3">
      <button
        class="rounded-lg border border-gray-300 px-4 py-2 text-sm font-medium text-gray-700 transition-colors hover:bg-gray-50"
        @click="emit('close')"
      >
        {{ cancelLabel }}
      </button>
      <button
        class="rounded-lg bg-red-600 px-4 py-2 text-sm font-medium text-white transition-colors hover:bg-red-700 disabled:opacity-50"
        :disabled="busy"
        @click="emit('confirm')"
      >
        {{ confirmLabel }}
      </button>
    </div>
  </Modal>
</template>
