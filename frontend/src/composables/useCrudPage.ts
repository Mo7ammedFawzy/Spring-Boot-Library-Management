import { computed, onMounted, ref, type Ref } from 'vue'
import type { ColDef, GetRowIdParams, GridApi } from 'ag-grid-community'
import type { FormError } from '@nuxt/ui'
import { ApiError } from '../services/api'

export interface CrudPageConfig<T, TInput> {
  fetchFn: () => Promise<T[]>
  createFn: (input: TInput) => Promise<T>
  updateFn: (id: number, input: TInput) => Promise<T>
  deleteFn: (id: number) => Promise<void>
  getRowId: (params: GetRowIdParams) => string
  columns: ColDef<T>[]
  validate: (state: any) => FormError[]
  toInput: (state: any, editingItem: T | null) => TInput
  entityName: string
}

export function useCrudPage<T, TInput>(config: CrudPageConfig<T, TInput>) {
  const rows: Ref<T[]> = ref([])
  const gridApi = ref<GridApi<T> | null>(null)
  const search = ref('')
  const loadError = ref('')

  const formOpen = ref(false)
  const editingItem: Ref<T | null> = ref(null)
  const saving = ref(false)
  const formError = ref('')
  const formState = ref<any>({})
  const entityForm = ref<{ submit: () => Promise<void> } | null>(null)

  const deleteTarget: Ref<T | null> = ref(null)
  const deleting = ref(false)
  const deleteError = ref('')

  const deleteModalOpen = computed({
    get: () => deleteTarget.value !== null,
    set: (val) => {
      if (!val) deleteTarget.value = null
    }
  })

  async function load() {
    loadError.value = ''
    try {
      rows.value = await config.fetchFn()
    } catch (e) {
      loadError.value = e instanceof ApiError ? e.message : `Failed to load ${config.entityName}s.`
    }
  }

  onMounted(load)

  function openAdd() {
    editingItem.value = null
    formState.value = {}
    formError.value = ''
    formOpen.value = true
  }

  function openEdit(item: T) {
    editingItem.value = item
    formError.value = ''
    formOpen.value = true
  }

  function setFormState(state: any) {
    formState.value = state
  }

  async function submitForm() {
    saving.value = true
    formError.value = ''
    try {
      const input = config.toInput(formState.value, editingItem.value)
      if (editingItem.value) {
        const id = (editingItem.value as any).id
        await config.updateFn(id, input)
      } else {
        await config.createFn(input)
      }
      await load()
      formOpen.value = false
    } catch (e) {
      formError.value = e instanceof ApiError ? e.message : `Failed to save ${config.entityName}.`
    } finally {
      saving.value = false
    }
  }

  function openDelete(item: T) {
    deleteTarget.value = item
    deleteError.value = ''
  }

  async function confirmDelete() {
    if (!deleteTarget.value) return
    deleting.value = true
    deleteError.value = ''
    try {
      const id = (deleteTarget.value as any).id
      await config.deleteFn(id)
      await load()
      deleteTarget.value = null
    } catch (e) {
      deleteError.value = e instanceof ApiError ? e.message : `Failed to delete ${config.entityName}.`
    } finally {
      deleting.value = false
    }
  }

  return {
    rows,
    gridApi,
    search,
    loadError,
    formOpen,
    editingItem,
    saving,
    formError,
    formState,
    entityForm,
    deleteTarget,
    deleting,
    deleteError,
    deleteModalOpen,
    load,
    openAdd,
    openEdit,
    setFormState,
    submitForm,
    openDelete,
    confirmDelete,
    columns: config.columns
  }
}