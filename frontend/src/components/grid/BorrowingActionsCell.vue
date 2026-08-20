<script setup lang="ts">
import { computed } from 'vue'
import type { ICellRendererParams } from 'ag-grid-community'
import type { Borrowing } from '../../services/borrowings'

interface BorrowingActionsParams extends ICellRendererParams {
  onView?: (borrowing: Borrowing) => void
  onReturn?: (borrowing: Borrowing) => void
}

const props = defineProps<{ params: BorrowingActionsParams }>()

const items = computed(() => [
  {
    label: 'View Details',
    icon: 'i-lucide-eye',
    onSelect: () => props.params.onView?.(props.params.data as Borrowing)
  },
  {
    label: 'Return Book',
    icon: 'i-lucide-book-check',
    onSelect: () => props.params.onReturn?.(props.params.data as Borrowing)
  }
])
</script>

<template>
  <div class="flex h-full items-center justify-end gap-0.5">
    <UButton
      icon="i-lucide-eye"
      color="neutral"
      variant="ghost"
      size="sm"
      aria-label="View details"
      @click="props.params.onView?.(props.params.data as Borrowing)"
    />
    <UDropdownMenu :items="items">
      <UButton
        icon="i-lucide-more-horizontal"
        color="neutral"
        variant="ghost"
        size="sm"
        aria-label="More actions"
      />
    </UDropdownMenu>
  </div>
</template>
