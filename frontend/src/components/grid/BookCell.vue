<script setup lang="ts">
import { computed } from 'vue'
import type { ICellRendererParams } from 'ag-grid-community'
import type { Borrowing } from '../../services/borrowings'

const props = defineProps<{ params: ICellRendererParams }>()

const borrowing = computed(() => props.params.data as Borrowing)
const authors = computed(() => borrowing.value.book.authors.map((author) => author.name).join(', '))
</script>

<template>
  <div class="flex h-full flex-col justify-center">
    <p class="truncate text-sm font-medium text-highlighted">
      {{ borrowing.book.title }}
    </p>
    <p class="truncate text-xs text-muted">
      {{ authors }}
    </p>
  </div>
</template>
