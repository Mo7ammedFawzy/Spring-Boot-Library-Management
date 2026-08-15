<script setup lang="ts">
import { computed, onMounted, ref, type Ref } from 'vue'
import type { ColDef, GetRowIdParams, GridApi, ICellRendererParams } from 'ag-grid-community'
import ActionsCell from '../components/grid/ActionsCell.vue'
import type { FormError } from '@nuxt/ui'
import {
  createAuthor,
  deleteAuthor,
  fetchAuthors,
  updateAuthor,
  type Author
} from '../services/authors'
import { ApiError } from '../services/api'

const rows: Ref<Author[]> = ref([])
const gridApi = ref<GridApi<Author> | null>(null)
const search = ref('')
const loadError = ref('')

async function loadAuthors() {
  loadError.value = ''
  try {
    rows.value = await fetchAuthors()
  } catch (e) {
    loadError.value = e instanceof ApiError ? e.message : 'Failed to load authors.'
  }
}

onMounted(loadAuthors)

function getRowId(params: GetRowIdParams) {
  return String((params.data as Author).id)
}

const columns: ColDef<Author>[] = [
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
      onEdit: (params: ICellRendererParams) => openEdit(params.data as Author),
      onDelete: (params: ICellRendererParams) => openDelete(params.data as Author)
    }
  }
]

const formOpen = ref(false)
const editingAuthor: Ref<Author | null> = ref(null)
const saving = ref(false)
const formError = ref('')
const authorForm = ref<{ submit: () => Promise<void> } | null>(null)
const name = ref('')

function openAdd() {
  editingAuthor.value = null
  name.value = ''
  formError.value = ''
  formOpen.value = true
}

function openEdit(author: Author) {
  editingAuthor.value = author
  name.value = author.name
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
    if (editingAuthor.value) {
      await updateAuthor(editingAuthor.value.id, { name: name.value.trim() })
    } else {
      await createAuthor({ name: name.value.trim() })
    }
    await loadAuthors()
    formOpen.value = false
  } catch (e) {
    formError.value = e instanceof ApiError ? e.message : 'Failed to save author.'
  } finally {
    saving.value = false
  }
}

const deleteTarget: Ref<Author | null> = ref(null)
const deleting = ref(false)
const deleteError = ref('')

function openDelete(author: Author) {
  deleteTarget.value = author
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
    await deleteAuthor(deleteTarget.value.id)
    await loadAuthors()
    deleteTarget.value = null
  } catch (e) {
    deleteError.value = e instanceof ApiError ? e.message : 'Failed to delete author.'
  } finally {
    deleting.value = false
  }
}

const fieldUi = {
  base: '!rounded-lg !bg-white !py-3 !text-sm !ring-(--ui-border) !placeholder:text-muted focus-visible:!ring-2 focus-visible:!ring-primary focus-visible:!outline-none'
}
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
      class="mb-4 flex items-center gap-2 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700"
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
          class="h-[38px] w-full rounded-lg border border-(--ui-border) bg-white pl-9 pr-4 text-sm text-highlighted shadow-sm outline-none transition-colors placeholder:text-muted focus:border-primary focus:ring-1 focus:ring-primary"
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

    <div class="flex min-h-0 flex-1 flex-col overflow-hidden rounded-xl bg-white shadow-sm ring-1 ring-(--ui-border)">
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
        content: '!max-w-md !rounded-xl !bg-white !shadow-[0_8px_32px_rgba(0,0,0,0.04)] !ring-0 border border-(--ui-border)'
      }"
    >
      <template #content="{ close }">
        <div class="flex flex-col">
          <div class="flex shrink-0 items-center justify-between gap-4 border-b border-(--ui-border) px-4 py-3">
            <div class="flex items-center gap-4">
              <div class="flex h-12 w-12 shrink-0 items-center justify-center rounded-full bg-[#f5f3f2]">
                <UIcon
                  name="i-lucide-user"
                  class="text-lg text-highlighted"
                />
              </div>
              <div>
                <h2 class="font-display text-[20px] font-semibold leading-tight text-highlighted">
                  {{ editingAuthor ? 'Edit Author' : 'Add Author' }}
                </h2>
                <p class="text-xs font-medium text-muted">
                  {{ editingAuthor ? 'Update author name' : 'Add a new author to your library' }}
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
              class="mb-4 flex items-center gap-2 rounded-lg border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-700"
            >
              <UIcon
                name="i-lucide-alert-circle"
                class="size-4 shrink-0"
              />
              <span>{{ formError }}</span>
            </div>

            <UForm
              ref="authorForm"
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

          <div class="flex shrink-0 items-center justify-end gap-2 border-t border-(--ui-border) bg-[#f5f3f2] px-4 py-3">
            <UButton
              color="neutral"
              variant="outline"
              size="lg"
              class="!rounded-lg !px-8 !py-2.5 !bg-transparent ring-(--ui-border-accented) hover:!bg-[#f5f3f2]"
              @click="close"
            >
              Cancel
            </UButton>
            <UButton
              color="primary"
              variant="solid"
              :icon="editingAuthor ? 'i-lucide-save' : 'i-lucide-user'"
              size="lg"
              class="!rounded-lg !px-8 !py-2.5 !bg-brand-700 hover:!bg-brand-600"
              :loading="saving"
              @click="authorForm?.submit()"
            >
              {{ editingAuthor ? 'Save Changes' : 'Add Author' }}
            </UButton>
          </div>
        </div>
      </template>
    </UModal>

    <UModal
      v-model:open="deleteModalOpen"
      :ui="{
        overlay: '!bg-black/20 backdrop-blur-sm',
        content: '!max-w-md !rounded-xl !bg-white !shadow-[0_8px_32px_rgba(0,0,0,0.04)] !ring-0 border border-(--ui-border)'
      }"
    >
      <template #content>
        <div class="flex flex-col">
          <div class="flex shrink-0 items-center justify-between gap-4 border-b border-(--ui-border) px-4 py-3">
            <div class="flex items-center gap-4">
              <div class="flex h-12 w-12 shrink-0 items-center justify-center rounded-full bg-red-50">
                <UIcon
                  name="i-lucide-trash-2"
                  class="text-lg text-red-600"
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
              class="mt-3 flex items-center gap-2 rounded-lg border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-700"
            >
              <UIcon
                name="i-lucide-alert-circle"
                class="size-4 shrink-0"
              />
              <span>{{ deleteError }}</span>
            </div>
          </div>

          <div class="flex shrink-0 items-center justify-end gap-2 border-t border-(--ui-border) bg-[#f5f3f2] px-4 py-3">
            <UButton
              color="neutral"
              variant="outline"
              size="lg"
              class="!rounded-lg !px-8 !py-2.5 !bg-transparent ring-(--ui-border-accented) hover:!bg-[#f5f3f2]"
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
