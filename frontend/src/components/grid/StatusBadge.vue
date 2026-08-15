<script setup lang="ts">
import type { BorrowingStatus } from '../../services/borrowings'

defineProps<{ status: BorrowingStatus }>()

const badgeColor: Record<BorrowingStatus, 'error' | 'warning' | 'success' | 'neutral'> = {
  Overdue: 'error',
  'Due Today': 'warning',
  Borrowed: 'success',
  Returned: 'neutral'
}
</script>

<template>
  <UBadge
    :color="badgeColor[status]"
    size="md"
    variant="subtle"
  >
    {{ status }}
  </UBadge>
</template>
