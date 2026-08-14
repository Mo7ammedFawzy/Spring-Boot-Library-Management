<script setup lang="ts">
import type { ICellRendererParams } from 'ag-grid-community'
import type { Book } from '../../data/books'
import { statusColor } from '../../data/books'

const props = defineProps<{
  params: ICellRendererParams<Book, Book['status']>
}>()
</script>

<template>
  <div class="flex h-full items-center">
    <UBadge
      :color="statusColor[props.params.value]"
      variant="soft"
      size="sm"
      :ui="{ base: 'rounded-[4px]' }"
    >
      {{ props.params.value }}
    </UBadge>
  </div>
</template>
