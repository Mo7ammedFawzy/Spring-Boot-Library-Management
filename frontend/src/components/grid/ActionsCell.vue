<script setup lang="ts">
import type { ICellRendererParams } from 'ag-grid-community'
import type { Book } from '../../data/books'

defineProps<{
  params: ICellRendererParams<Book>
}>()
</script>

<template>
  <div class="flex h-full items-center justify-end gap-0.5">
    <UButton
      icon="i-lucide-pencil"
      color="neutral"
      variant="ghost"
      size="sm"
      aria-label="Edit book"
    />
    <UButton
      icon="i-lucide-more-horizontal"
      color="neutral"
      variant="ghost"
      size="sm"
      aria-label="Book actions"
    />
  </div>
</template>
