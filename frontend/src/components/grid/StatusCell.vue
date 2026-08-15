<script setup lang="ts">
import { computed } from 'vue'
import type { ICellRendererParams } from 'ag-grid-community'
import { getStatus, type Borrowing } from '../../services/borrowings'
import StatusBadge from './StatusBadge.vue'

const props = defineProps<{ params: ICellRendererParams }>()

const status = computed(() => getStatus(props.params.data as Borrowing))
</script>

<template>
  <div class="flex h-full items-center">
    <StatusBadge :status="status" />
  </div>
</template>
