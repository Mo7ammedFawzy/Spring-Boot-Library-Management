<script setup lang="ts">
import { computed } from 'vue'
import type { ICellRendererParams } from 'ag-grid-community'
import type { Borrowing } from '../../services/borrowings'

const props = defineProps<{ params: ICellRendererParams }>()

const borrowing = computed(() => props.params.data as Borrowing)

const avatarPalette = [
  'bg-[#E3F2FD] text-[#1565C0]',
  'bg-[#FFF3E0] text-[#B7791F]',
  'bg-[#E8F5E9] text-[#2E7D32]',
  'bg-[#F3E5F5] text-[#8E24AA]',
  'bg-[#E0F2F1] text-[#00897B]',
  'bg-[#FCEEE8] text-[#9f3c11]'
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
