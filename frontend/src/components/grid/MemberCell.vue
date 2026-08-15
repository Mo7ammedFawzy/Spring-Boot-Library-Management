<script setup lang="ts">
import { computed } from 'vue'
import type { ICellRendererParams } from 'ag-grid-community'
import type { Borrowing } from '../../services/borrowings'

const props = defineProps<{ params: ICellRendererParams }>()

const borrowing = computed(() => props.params.data as Borrowing)

const avatarPalette = [
  'bg-blue-100 text-blue-700 dark:bg-blue-950/60 dark:text-blue-300',
  'bg-amber-100 text-amber-700 dark:bg-amber-950/60 dark:text-amber-300',
  'bg-green-100 text-green-700 dark:bg-green-950/60 dark:text-green-300',
  'bg-purple-100 text-purple-700 dark:bg-purple-950/60 dark:text-purple-300',
  'bg-teal-100 text-teal-700 dark:bg-teal-950/60 dark:text-teal-300',
  'bg-primary-100 text-primary-700 dark:bg-primary-950/60 dark:text-primary-300'
]

const avatarClass = computed(() => avatarPalette[(borrowing.value.user.id ?? 0) % avatarPalette.length])

const initials = computed(() => {
  const name = borrowing.value.user.name.trim()
  return (
    name
      .split(/\s+/)
      .slice(0, 2)
      .map((word) => word[0]?.toUpperCase() ?? '')
      .join('') || '?'
  )
})
</script>

<template>
  <div class="flex h-full items-center gap-3">
    <div
      class="flex h-8 w-8 shrink-0 items-center justify-center rounded-full text-[10px] font-bold"
      :class="avatarClass"
    >
      {{ initials }}
    </div>
    <div class="min-w-0">
      <p class="truncate text-sm font-medium text-highlighted">
        {{ borrowing.user.name }}
      </p>
      <p class="truncate text-xs text-muted">
        {{ borrowing.user.email }}
      </p>
    </div>
  </div>
</template>
