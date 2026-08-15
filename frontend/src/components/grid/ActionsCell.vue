<script setup lang="ts">
import type { ICellRendererParams } from 'ag-grid-community'

interface ActionsCellParams extends ICellRendererParams {
  onEdit?: (params: ICellRendererParams) => void
  onDelete?: (params: ICellRendererParams) => void
}

const props = defineProps<{
  params: ActionsCellParams
}>()
</script>

<template>
  <div class="flex h-full items-center justify-end gap-0.5">
    <UButton
      v-if="props.params.onEdit"
      icon="i-lucide-pencil"
      color="neutral"
      variant="ghost"
      size="sm"
      aria-label="Edit"
      @click="props.params.onEdit?.(props.params)"
    />
    <UButton
      v-if="props.params.onDelete"
      icon="i-lucide-trash-2"
      color="neutral"
      variant="ghost"
      size="sm"
      aria-label="Delete"
      class="hover:!bg-red-50 hover:!text-red-600"
      @click="props.params.onDelete?.(props.params)"
    />
  </div>
</template>
