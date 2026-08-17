<script setup lang="ts">
import { computed, onMounted, ref, type Ref } from 'vue'
import type { ColDef, GetRowIdParams, GridApi, ICellRendererParams } from 'ag-grid-community'
import ActionsCell from '../components/grid/ActionsCell.vue'
import type { FormError, BreadcrumbItem } from '@nuxt/ui'
import {
  createCategory,
  deleteCategory,
  fetchCategories,
  updateCategory,
  type Category
} from '../services/categories'
import { ApiError } from '../services/api'

const breadcrumbItems = ref<BreadcrumbItem[]>([
  { label: 'Dashboard', icon: 'i-lucide-layout-dashboard', to: '/dashboard' },
  { label: 'Categories', icon: 'i-lucide-folder-open' }
])

const rows: Ref<Category[]> = ref([])
const gridApi = ref<GridApi<Category> | null>(null)
const search = ref('')
const loadError = ref('')

async function loadCategories() {
  loadError.value = ''
  try {
    rows.value = await fetchCategories()
  } catch (e) {
    loadError.value = e instanceof ApiError ? e.message : 'Failed to load categories.'
  }
}

onMounted(loadCategories)

function getRowId(params: GetRowIdParams) {
  return String((params.data as Category).id)
}

const columns: ColDef<Category>[] = [
  {
    headerName: '#',
    sortable: false,
    filter: false,
    width: 48,
    valueGetter: (params) => (params.node?.rowIndex ?? 0) + 1,
    cellStyle: { textAlign: 'center' },
    cellClass: 'text-dimmed'
  },
  {
    field: 'name',
    headerName: 'Name',
    flex: 2,
    cellStyle: { fontWeight: 500 },
    cellClass: 'text-highlighted'
  },
  {
    headerName: 'Actions',
    sortable: false,
    filter: false,
    width: 104,
    pinned: 'right',
    headerClass: 'ag-right-aligned-header',
    cellRenderer: ActionsCell,
    cellRendererParams: {
      onEdit: (params: ICellRendererParams) => openEdit(params.data as Category),
      onDelete: (params: ICellRendererParams) => openDelete(params.data as Category)
    }
  }
]

const formOpen = ref(false)
const editingCategory: Ref<Category | null> = ref(null)
const saving = ref(false)
const formError = ref('')
const categoryForm = ref<{ submit: () => Promise<void> } | null>(null)
const name = ref('')

function openAdd() {
  editingCategory.value = null
  name.value = ''
  formError.value = ''
  formOpen.value = true
}

function openEdit(category: Category) {
  editingCategory.value = category
  name.value = category.name
  formError.value = ''
  formOpen.value = true
}

function validateName(state: { name: string }): FormError[] {
  const errors: FormError[] = []
  if (!state.name.trim()) {
    errors.push({ name: 'name', message: 'Name is required' })
  }
  return errors
}

async function submitForm() {
  saving.value = true
  formError.value = ''
  try {
    if (editingCategory.value) {
      await updateCategory(editingCategory.value.id, { name: name.value.trim() })
    } else {
      await createCategory({ name: name.value.trim() })
    }
    await loadCategories()
    formOpen.value = false
  } catch (e) {
    formError.value = e instanceof ApiError ? e.message : 'Failed to save category.'
  } finally {
    saving.value = false
  }
}

const deleteTarget: Ref<Category | null> = ref(null)
const deleting = ref(false)
const deleteError = ref('')

function openDelete(category: Category) {
  deleteTarget.value = category
  deleteError.value = ''
}

const deleteModalOpen = computed({
  get: () => deleteTarget.value !== null,
  set: (val) => {
    if (!val) deleteTarget.value = null
  }
})

async function confirmDelete() {
  if (!deleteTarget.value) return
  deleting.value = true
  deleteError.value = ''
  try {
    await deleteCategory(deleteTarget.value.id)
    await loadCategories()
    deleteTarget.value = null
  } catch (e) {
    deleteError.value = e instanceof ApiError ? e.message : 'Failed to delete category.'
  } finally {
    deleting.value = false
  }
}

const fieldUi = {
  base: '!rounded-lg !bg-(--ui-bg-card) !py-3 !text-sm !ring-(--ui-border) !placeholder:text-muted focus-visible:!ring-2 focus-visible:!ring-primary focus-visible:!outline-none'
}
</script>

<template>
  <div class="flex min-h-0 flex-1 flex-col">
    <div class="mb-4">
      <UBreadcrumb class="mb-2" :items="breadcrumbItems">
        <template #item="{ item }">
          <span
            :class="[
              'flex items-center gap-1.5 text-sm transition-colors',
              item.to ? 'text-muted hover:text-highlighted cursor-pointer' : 'font-semibold text-highlighted'
            ]"
          >
            <UIcon
              v-if="item.icon"
              :name="item.icon"
              class="size-4 text-muted"
            />
            {{ item.label }}
          </span>
        </template>
        <template #separator>
          <UIcon name="i-lucide-chevron-right" class="size-3.5 text-muted" />
        </template>
      </UBreadcrumb>
      <p class="text-sm text-muted">
        Organize the library collection by category.
      </p>
    </div>

    <div
      v-if="loadError"
      class="mb-4 flex items-center gap-2 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700 dark:border-red-950 dark:bg-red-950/40 dark:text-red-300"
    >
      <UIcon
        name="i-lucide-alert-circle"
        class="size-4 shrink-0"
      />
      <span>{{ loadError }}</span>
    </div>

    <div class="mb-3 flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
      <div class="relative w-full md:w-[400px]">
        <UIcon
          name="i-lucide-search"
          class="pointer-events-none absolute left-3 top-1/2 size-[18px] -translate-y-1/2 text-muted"
        />
        <input
          v-model="search"
          type="text"
          placeholder="Search categories by name..."
          class="h-[38px] w-full rounded-lg border border-(--ui-border) bg-(--ui-bg-card) pl-9 pr-4 text-sm text-highlighted shadow-sm outline-none transition-colors placeholder:text-muted focus:border-primary focus:ring-1 focus:ring-primary"
        >
      </div>

      <UButton
        icon="i-lucide-plus"
        class="!h-[38px] !rounded-lg !px-4 shadow-sm"
        @click="openAdd"
      >
        Add Category
      </UButton>
    </div>

    <div class="flex min-h-0 flex-1 flex-col overflow-hidden rounded-xl bg-(--ui-bg-card) shadow-sm ring-1 ring-(--ui-border)">
      <AppDataGrid
        v-model:api="gridApi"
        :rows="rows"
        :columns="columns"
        :quick-filter-text="search"
        :get-row-id="getRowId"
        height="100%"
      />
    </div>

    <UModal
      v-model:open="formOpen"
      :ui="{
        overlay: '!bg-black/20 backdrop-blur-sm',
        content: '!max-w-md !rounded-xl !bg-(--ui-bg-card) !shadow-[0_8px_32px_rgba(0,0,0,0.04)] !ring-0 border border-(--ui-border)'
      }"
    >
      <template #content="{ close }">
        <div class="flex flex-col">
          <div class="flex shrink-0 items-center justify-between gap-4 border-b border-(--ui-border) px-4 py-3">
            <div class="flex items-center gap-4">
              <div class="flex h-12 w-12 shrink-0 items-center justify-center rounded-full bg-(--ui-bg-accented)">
                <UIcon
                  name="i-lucide-tags"
                  class="text-lg text-highlighted"
                />
              </div>
              <div>
                <h2 class="font-display text-[20px] font-semibold leading-tight text-highlighted">
                  {{ editingCategory ? 'Edit Category' : 'Add Category' }}
                </h2>
                <p class="text-xs font-medium text-muted">
                  {{ editingCategory ? 'Update category name' : 'Add a new category to your library' }}
                </p>
              </div>
            </div>

            <UButton
              color="neutral"
              variant="ghost"
              icon="i-lucide-x"
              aria-label="Close modal"
              @click="close"
            />
          </div>

          <div class="grow px-4 py-4">
            <div
              v-if="formError"
              class="mb-4 flex items-center gap-2 rounded-lg border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-700 dark:border-red-950 dark:bg-red-950/40 dark:text-red-300"
            >
              <UIcon
                name="i-lucide-alert-circle"
                class="size-4 shrink-0"
              />
              <span>{{ formError }}</span>
            </div>

            <UForm
              ref="categoryForm"
              :state="{ name }"
              :validate="validateName"
              class="flex flex-col gap-2"
              @submit="submitForm"
            >
              <label
                for="category-name"
                class="text-sm font-medium text-highlighted"
              >
                Name <span class="text-error">*</span>
              </label>
              <UInput
                id="category-name"
                v-model="name"
                placeholder="e.g. Science Fiction"
                :ui="fieldUi"
              />
            </UForm>
          </div>

          <div class="flex shrink-0 items-center justify-end gap-2 border-t border-(--ui-border) bg-(--ui-bg-accented) px-4 py-3">
            <UButton
              color="neutral"
              variant="outline"
              size="lg"
              class="!rounded-lg !px-8 !py-2.5 !bg-transparent ring-(--ui-border-accented) hover:!bg-(--ui-bg-accented)"
              @click="close"
            >
              Cancel
            </UButton>
            <UButton
              color="primary"
              variant="solid"
              :icon="editingCategory ? 'i-lucide-save' : 'i-lucide-tag'"
              size="lg"
              class="!rounded-lg !px-8 !py-2.5 !bg-brand-700 dark:!bg-primary-400 hover:!bg-brand-600 dark:hover:!bg-primary-300"
              :loading="saving"
              @click="categoryForm?.submit()"
            >
              {{ editingCategory ? 'Save Changes' : 'Add Category' }}
            </UButton>
          </div>
        </div>
      </template>
    </UModal>

    <UModal
      v-model:open="deleteModalOpen"
      :ui="{
        overlay: '!bg-black/20 backdrop-blur-sm',
        content: '!max-w-md !rounded-xl !bg-(--ui-bg-card) !shadow-[0_8px_32px_rgba(0,0,0,0.04)] !ring-0 border border-(--ui-border)'
      }"
    >
      <template #content>
        <div class="flex flex-col">
          <div class="flex shrink-0 items-center justify-between gap-4 border-b border-(--ui-border) px-4 py-3">
            <div class="flex items-center gap-4">
              <div class="flex h-12 w-12 shrink-0 items-center justify-center rounded-full bg-red-50 dark:bg-red-950/50">
                <UIcon
                  name="i-lucide-trash-2"
                  class="text-lg text-red-600 dark:text-red-400"
                />
              </div>
              <div>
                <h2 class="font-display text-[20px] font-semibold leading-tight text-highlighted">
                  Delete Category?
                </h2>
                <p class="text-xs font-medium text-muted">
                  This action cannot be undone
                </p>
              </div>
            </div>

            <UButton
              color="neutral"
              variant="ghost"
              icon="i-lucide-x"
              aria-label="Close modal"
              @click="deleteTarget = null"
            />
          </div>

          <div class="px-4 py-4">
            <p class="text-sm leading-relaxed text-highlighted">
              Are you sure you want to delete
              <span class="font-semibold">"{{ deleteTarget?.name }}"</span>?
            </p>

            <div
              v-if="deleteError"
              class="mt-3 flex items-center gap-2 rounded-lg border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-700 dark:border-red-950 dark:bg-red-950/40 dark:text-red-300"
            >
              <UIcon
                name="i-lucide-alert-circle"
                class="size-4 shrink-0"
              />
              <span>{{ deleteError }}</span>
            </div>
          </div>

          <div class="flex shrink-0 items-center justify-end gap-2 border-t border-(--ui-border) bg-(--ui-bg-accented) px-4 py-3">
            <UButton
              color="neutral"
              variant="outline"
              size="lg"
              class="!rounded-lg !px-8 !py-2.5 !bg-transparent ring-(--ui-border-accented) hover:!bg-(--ui-bg-accented)"
              @click="deleteTarget = null"
            >
              Cancel
            </UButton>
            <UButton
              color="error"
              variant="solid"
              icon="i-lucide-trash-2"
              size="lg"
              class="!rounded-lg !px-8 !py-2.5"
              :loading="deleting"
              @click="confirmDelete"
            >
              Delete
            </UButton>
          </div>
        </div>
      </template>
    </UModal>
  </div>
</template>
