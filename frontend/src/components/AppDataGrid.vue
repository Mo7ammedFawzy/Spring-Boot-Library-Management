<script setup lang="ts">
import { computed, watch } from 'vue'
import { useColorMode } from '@vueuse/core'
import { AgGridVue } from 'ag-grid-vue3'
import { themeQuartz, type ColDef, type GetRowIdParams, type GridApi, type GridReadyEvent } from 'ag-grid-community'
import '../lib/agGrid'

const props = withDefaults(defineProps<{
  rows: unknown[]
  columns: ColDef[]
  getRowId?: (params: GetRowIdParams) => string
  quickFilterText?: string
  rowHeight?: number
  headerHeight?: number
  pageSize?: number
  pageSizeSelector?: number[]
  height?: string
}>(), {
  getRowId: (params: GetRowIdParams) => String((params.data as { id: number | string }).id),
  quickFilterText: '',
  rowHeight: 40,
  headerHeight: 40,
  pageSize: 10,
  pageSizeSelector: () => [10, 20, 50, 100],
  height: '100%'
})

const emit = defineEmits<{
  (e: 'grid-ready', params: GridReadyEvent): void
}>()

const gridApi = defineModel<GridApi | null>('api', { default: null })

const lightTheme = themeQuartz.withParams({
  fontFamily: "'Inter Variable', ui-sans-serif, system-ui, sans-serif",
  fontSize: 14,
  headerFontFamily: "'Inter Variable', ui-sans-serif, system-ui, sans-serif",
  headerFontSize: 11,
  headerFontWeight: 600,
  headerTextColor: '#6e6863',
  headerBackgroundColor: '#ffffff',
  backgroundColor: '#ffffff',
  textColor: '#1b1c1b',
  subtleTextColor: '#8a847e',
  borderColor: '#e5e1e0',
  borderWidth: 1,
  wrapperBorder: false,
  headerRowBorder: { color: '#e5e1e0', width: 1, style: 'solid' },
  rowBorder: { color: '#e5e1e0', width: 1, style: 'solid' },
  columnBorder: false,
  rowHoverColor: '#f7f5f4',
  rowHeight: props.rowHeight,
  headerHeight: props.headerHeight,
  paginationPanelHeight: 48,
  spacing: 8,
  iconSize: 16
})

const darkTheme = themeQuartz.withParams({
  fontFamily: "'Inter Variable', ui-sans-serif, system-ui, sans-serif",
  fontSize: 14,
  headerFontFamily: "'Inter Variable', ui-sans-serif, system-ui, sans-serif",
  headerFontSize: 11,
  headerFontWeight: 600,
  headerTextColor: '#a8a29e',
  headerBackgroundColor: '#292524',
  backgroundColor: '#292524',
  textColor: '#e7e5e4',
  subtleTextColor: '#78716c',
  borderColor: '#3a3532',
  borderWidth: 1,
  wrapperBorder: false,
  headerRowBorder: { color: '#3a3532', width: 1, style: 'solid' },
  rowBorder: { color: '#3a3532', width: 1, style: 'solid' },
  columnBorder: false,
  rowHoverColor: '#35302c',
  rowHeight: props.rowHeight,
  headerHeight: props.headerHeight,
  paginationPanelHeight: 48,
  spacing: 8,
  iconSize: 16
})

const colorMode = useColorMode()
const theme = computed(() => (colorMode.value === 'dark' ? darkTheme : lightTheme))

const defaultColDef: ColDef = {
  sortable: true,
  resizable: true,
  filter: false,
  minWidth: 90,
  suppressHeaderMenuButton: true
}

function onGridReady(params: GridReadyEvent) {
  gridApi.value = params.api
  params.api.setGridOption('quickFilterText', props.quickFilterText)
  emit('grid-ready', params)
}

watch(() => props.quickFilterText, (value) => {
  gridApi.value?.setGridOption('quickFilterText', value)
})
</script>

<template>
  <AgGridVue
    class="ag-theme-athenaeum"
    :style="{ width: '100%', height, flex: '1 1 0%', minHeight: '0' }"
    :theme="theme"
    :column-defs="columns"
    :row-data="rows"
    :default-col-def="defaultColDef"
    :get-row-id="getRowId"
    :pagination="true"
    :pagination-page-size="pageSize"
    :pagination-page-size-selector="pageSizeSelector"
    @grid-ready="onGridReady"
  />
</template>

<style scoped>
:deep(.ag-header-cell-text) {
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

:deep(.ag-header-row) {
  border-bottom-width: 1px;
}

:deep(.ag-root-wrapper) {
  border-radius: 0.75rem;
}

:deep(.ag-paging-panel) {
  border-top-width: 1px;
  font-size: 13px;
  color: var(--ui-text-muted);
}
</style>
