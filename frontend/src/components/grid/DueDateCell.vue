<script setup lang="ts">
import { computed } from 'vue'
import type { ICellRendererParams } from 'ag-grid-community'
import { daysUntil, formatDate, getStatus, type Borrowing } from '../../services/borrowings'

const props = defineProps<{ params: ICellRendererParams }>()

const borrowing = computed(() => props.params.data as Borrowing)
const status = computed(() => getStatus(borrowing.value))

const subtext = computed(() => {
  if (status.value === 'Overdue') return `Overdue by ${-daysUntil(borrowing.value.dueDate)} days`
  if (status.value === 'Due Today') return 'Due today'
  return null
})
</script>

<template>
  <div class="flex h-full flex-col justify-center">
    <p class="text-sm text-highlighted">
      {{ formatDate(borrowing.dueDate) }}
    </p>
    <p
      v-if="subtext"
      class="text-xs"
      :class="status === 'Overdue' ? 'text-error' : 'text-[#B7791F]'"
    >
      {{ subtext }}
    </p>
  </div>
</template>
