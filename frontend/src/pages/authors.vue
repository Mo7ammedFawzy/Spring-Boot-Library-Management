<script setup lang="ts">
import { ref, watch } from 'vue'
import type { ColDef } from 'ag-grid-community'
import ActionsCell from '../components/grid/ActionsCell.vue'
import type { FormError, BreadcrumbItem } from '@nuxt/ui'
import {
  createAuthor,
  deleteAuthor,
  fetchAuthors,
  updateAuthor,
  type Author
} from '../services/authors'
import { useCrudPage } from '../composables/useCrudPage'

const breadcrumbItems = ref<BreadcrumbItem[]>([
  { label: 'Dashboard', icon: 'i-lucide-layout-dashboard', to: '/dashboard' },
  { label: 'Authors', icon: 'i-lucide-user' }
])

const fieldUi = {
  base: '!rounded-lg !bg-(--ui-bg-card) !py-3 !text-sm !ring-(--ui-border) !placeholder:text-muted focus-visible:!ring-2 focus-visible:!ring-primary focus-visible:!outline-none'
}

function validateName(state: { name: string }): FormError[] {
  const errors: FormError[] = []
  if (!state.name.trim()) {
    errors.push({ name: 'name', message: 'Name is required' })
  }
  return errors
}

function getRowId(params: any) {
  return String((params.data as Author).id)
}

const columns: ColDef[] = [
  {
    headerName: '#',
    sortable: false,
    filter: false,
    width: 48,
    valueGetter: (params: any) => (params.node?.rowIndex ?? 0) + 1,
    cellStyle: { textAlign: 'center' },
    cellClass: 'text-dimmed'
  },
  {
    field: 'name',
    headerName: 'Name',
    flex: 2,
    cellStyle: { fontWeight: '500' },
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
      onEdit: (params: any) => openEdit(params.data as Author),
      onDelete: (params: any) => openDelete(params.data as Author)
    }
  }
]

const {
  rows,
  gridApi,
  search,
  loadError,
  formOpen,
  editingItem,
  saving,
  formError,
  entityForm,
  deleteTarget,
  deleting,
  deleteError,
  deleteModalOpen,
  openAdd,
  openEdit,
  setFormState,
  submitForm,
  openDelete,
  confirmDelete
} = useCrudPage<Author, { name: string }>({
  fetchFn: fetchAuthors,
  createFn: createAuthor,
  updateFn: updateAuthor,
  deleteFn: deleteAuthor,
  getRowId,
  columns,
  validate: validateName,
  toInput: (state: { name: string }) => ({ name: state.name.trim() }),
  entityName: 'author'
})

const name = ref('')

watch(formOpen, (open: boolean) => {
  if (open) {
    if (editingItem.value) {
      name.value = (editingItem.value as Author).name
    } else {
      name.value = ''
    }
  }
})

watch(name, (val: string) => {
  setFormState({ name: val })
})
</script>

<template>
  <div class="flex min-h-0 flex-1 flex-col">
    <div class="mb-4">
      <h1 class="font-display text-[28px] font-semibold leading-9 tracking-tight text-highlighted">
        Authors
      </h1>
      <p class="mt-1 text-sm text-muted">
        Browse the authors in the library catalog.
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
          placeholder="Search authors by name..."
          class="h-[38px] w-full rounded-lg border border-(--ui-border) bg-(--ui-bg-card) pl-9 pr-4 text-sm text-highlighted shadow-sm outline-none transition-colors placeholder:text-muted focus:border-primary focus:ring-1 focus:ring-primary"
        >
      </div>

      <UButton
        icon="i-lucide-plus"
        class="!h-[38px] !rounded-lg !px-4 shadow-sm"
        @click="openAdd"
      >
        Add Author
      </UButton>
    </div>

    <div class="flex min-h-0 flex-1 flex-col overflow-hidden rounded-xl bg-(--ui-bg-card) shadow-sm ring-1 ring-(--ui-border)">
      <AppDataGrid
        v-model:api="gridApi"
        :rows="rows"
        :columns="columns"
        :quick-filter-text="search"
        :get-row-id="(params) => String((params.data as Author).id)"
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
                  name="i-lucide-user"
                  class="text-lg text-highlighted"
                />
              </div>
              <div>
                <h2 class="font-display text-[20px] font-semibold leading-tight text-highlighted">
                  {{ editingItem ? 'Edit Author' : 'Add Author' }}
                </h2>
                <p class="text-xs font-medium text-muted">
                  {{ editingItem ? 'Update author name' : 'Add a new author to your library' }}
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
              ref="entityForm"
              :state="{ name }"
              :validate="validateName"
              class="flex flex-col gap-2"
              @submit="submitForm"
            >
              <label
                for="author-name"
                class="text-sm font-medium text-highlighted"
              >
                Name <span class="text-error">*</span>
              </label>
              <UInput
                id="author-name"
                v-model="name"
                placeholder="e.g. J.R.R. Tolkien"
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
              :icon="editingItem ? 'i-lucide-save' : 'i-lucide-user'"
              size="lg"
              class="!rounded-lg !px-8 !py-2.5 !bg-brand-700 dark:!bg-primary-400 hover:!bg-brand-600 dark:hover:!bg-primary-300"
              :loading="saving"
              @click="entityForm?.submit()"
            >
              {{ editingItem ? 'Save Changes' : 'Add Author' }}
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
                  Delete Author?
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