<script setup lang="ts">
import { computed, onMounted, ref, type Ref, watch } from 'vue'
import type { ColDef, GetRowIdParams, GridApi, ICellRendererParams } from 'ag-grid-community'
import ActionsCell from '../components/grid/ActionsCell.vue'
import type { SelectMenuItem, FormError } from '@nuxt/ui'
import {
  createBook,
  deleteBook,
  fetchBooks,
  updateBook,
  type Book,
  type BookInput
} from '../services/books'
import { fetchCategories, type Category } from '../services/categories'
import { fetchAuthors, type Author } from '../services/authors'
import { ApiError } from '../services/api'

const rows: Ref<Book[]> = ref([])
const categories: Ref<Category[]> = ref([])
const authors: Ref<Author[]> = ref([])
const gridApi = ref<GridApi<Book> | null>(null)
const search = ref('')
const loadError = ref('')

const categoryFilters = ref<string[]>([])

async function loadBooks() {
  rows.value = await fetchBooks()
}

async function loadOptions() {
  const [cats, auths] = await Promise.all([fetchCategories(), fetchAuthors()])
  categories.value = cats
  authors.value = auths
}

async function loadAll() {
  loadError.value = ''
  try {
    await Promise.all([loadBooks(), loadOptions()])
  } catch (e) {
    loadError.value = e instanceof ApiError ? e.message : 'Failed to load books.'
  }
}

onMounted(loadAll)

function getRowId(params: GetRowIdParams) {
  return String((params.data as Book).id)
}

const columns: ColDef<Book>[] = [
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
    field: 'title',
    headerName: 'Title',
    flex: 2,
    cellStyle: { fontWeight: 500 },
    cellClass: 'text-highlighted'
  },
  {
    headerName: 'Authors',
    flex: 2,
    valueGetter: (params) => (params.data as Book).authors.map((author) => author.name).join(', ')
  },
  {
    headerName: 'Category',
    flex: 1,
    valueGetter: (params) => (params.data as Book).category.name,
    filter: 'agTextColumnFilter'
  },
  {
    field: 'availableCopies',
    headerName: 'Copies',
    width: 90,
    sortable: true,
    filter: false
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
      onEdit: (params: ICellRendererParams) => openEdit(params.data as Book),
      onDelete: (params: ICellRendererParams) => openDelete(params.data as Book)
    }
  }
]

function applyFilters() {
  const api = gridApi.value
  if (!api) return

  if (categoryFilters.value.length === 0) {
    api.setColumnFilterModel('category', null)
  } else {
    api.setColumnFilterModel('category', {
      filterType: 'text',
      operator: 'OR',
      conditions: categoryFilters.value.map((category) => ({
        filterType: 'text',
        type: 'equals',
        filter: category
      }))
    })
  }

  api.onFilterChanged()
}

watch(categoryFilters, applyFilters)

function resetFilters() {
  categoryFilters.value = []
}

interface BookFormState {
  title: string
  description: string
  categoryId: number | null
  authorIds: number[]
  availableCopies: number
}

const formOpen = ref(false)
const editingBook: Ref<Book | null> = ref(null)
const saving = ref(false)
const formError = ref('')
const bookForm = ref<{ submit: () => Promise<void> } | null>(null)

const form = ref<BookFormState>({
  title: '',
  description: '',
  categoryId: null,
  authorIds: [],
  availableCopies: 1
})

const categoryOptions = computed<SelectMenuItem[]>(() =>
  categories.value.map((category) => ({ label: category.name, value: category.id }))
)

const authorOptions = computed<SelectMenuItem[]>(() =>
  authors.value.map((author) => ({ label: author.name, value: author.id }))
)

function openAdd() {
  editingBook.value = null
  form.value = {
    title: '',
    description: '',
    categoryId: categories.value[0]?.id ?? null,
    authorIds: [],
    availableCopies: 1
  }
  formError.value = ''
  formOpen.value = true
}

function openEdit(book: Book) {
  editingBook.value = book
  form.value = {
    title: book.title,
    description: book.description,
    categoryId: book.category.id,
    authorIds: book.authors.map((author) => author.id),
    availableCopies: book.availableCopies
  }
  formError.value = ''
  formOpen.value = true
}

function validateBook(state: BookFormState): FormError[] {
  const errors: FormError[] = []
  if (!state.title.trim()) {
    errors.push({ name: 'title', message: 'Title is required' })
  }
  if (!state.description.trim()) {
    errors.push({ name: 'description', message: 'Description is required' })
  } else if (state.description.trim().length < 10) {
    errors.push({ name: 'description', message: 'Description must be at least 10 characters' })
  }
  if (state.categoryId == null) {
    errors.push({ name: 'categoryId', message: 'Category is required' })
  }
  if (state.authorIds.length === 0) {
    errors.push({ name: 'authorIds', message: 'At least one author is required' })
  }
  return errors
}

function toInput(state: BookFormState): BookInput {
  return {
    title: state.title.trim(),
    description: state.description.trim(),
    availableCopies: Number(state.availableCopies) || 1,
    categoryId: state.categoryId as number,
    authorIds: state.authorIds
  }
}

async function submitForm() {
  saving.value = true
  formError.value = ''
  try {
    const input = toInput(form.value)
    if (editingBook.value) {
      await updateBook(editingBook.value.id, input)
    } else {
      await createBook(input)
    }
    await loadBooks()
    formOpen.value = false
  } catch (e) {
    formError.value = e instanceof ApiError ? e.message : 'Failed to save book.'
  } finally {
    saving.value = false
  }
}

function incrementCopies() {
  form.value.availableCopies = Math.max(1, (Number(form.value.availableCopies) || 1) + 1)
}

function decrementCopies() {
  form.value.availableCopies = Math.max(1, (Number(form.value.availableCopies) || 1) - 1)
}

const deleteTarget: Ref<Book | null> = ref(null)
const deleting = ref(false)
const deleteError = ref('')

function openDelete(book: Book) {
  deleteTarget.value = book
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
    await deleteBook(deleteTarget.value.id)
    await loadBooks()
    deleteTarget.value = null
  } catch (e) {
    deleteError.value = e instanceof ApiError ? e.message : 'Failed to delete book.'
  } finally {
    deleting.value = false
  }
}

const fieldUi = {
  base: '!rounded-lg !bg-white !py-3 !text-sm !ring-(--ui-border) !placeholder:text-muted focus-visible:!ring-2 focus-visible:!ring-primary focus-visible:!outline-none'
}

const copiesUi = {
  ...fieldUi,
  trailing: '!pe-0',
  base: `${fieldUi.base} [appearance:textfield] [&::-webkit-inner-spin-button]:appearance-none [&::-webkit-outer-spin-button]:appearance-none`
}
</script>

<template>
  <div class="flex min-h-0 flex-1 flex-col">
    <div class="mb-4">

      <h1 class="font-display text-[28px] font-semibold leading-9 tracking-tight text-highlighted">
        Books
      </h1>
      <p class="mt-1 text-sm text-muted">
        Manage and organize all library books.
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
          placeholder="Search books by title, author or category..."
          class="h-[38px] w-full rounded-lg border border-(--ui-border) bg-white pl-9 pr-4 text-sm text-highlighted shadow-sm outline-none transition-colors placeholder:text-muted focus:border-primary focus:ring-1 focus:ring-primary"
        >
      </div>

      <div class="flex items-center gap-3">
        <UPopover>
          <UButton
            color="neutral"
            variant="outline"
            icon="i-lucide-filter"
            class="!h-[38px] !rounded-lg !bg-white !px-4 shadow-sm"
          >
            Filters
          </UButton>

          <template #content>
            <div class="flex w-64 flex-col gap-5 p-4">
              <div class="flex flex-col gap-2">
                <p class="text-[11px] font-semibold uppercase tracking-wide text-muted">
                  Category
                </p>
                <UCheckboxGroup
                  v-model="categoryFilters"
                  size="sm"
                  variant="list"
                  :items="categories.map((category) => category.name)"
                />
              </div>

              <UButton
                color="neutral"
                variant="ghost"
                size="sm"
                class="self-start"
                @click="resetFilters"
              >
                Clear filters
              </UButton>
            </div>
          </template>
        </UPopover>

        <UButton
          icon="i-lucide-plus"
          class="!h-[38px] !rounded-lg !px-4 shadow-sm"
          @click="openAdd"
        >
          Add Book
        </UButton>
      </div>
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
      :title="editingBook ? 'Edit Book' : 'Add Book'"
      :description="editingBook ? 'Update book details' : 'Add a new book to your library'"
      :ui="{
        overlay: '!bg-black/20 backdrop-blur-sm',
        content: '!max-w-2xl !rounded-xl !bg-white !shadow-[0_8px_32px_rgba(0,0,0,0.04)] !ring-0 border border-(--ui-border)'
      }"
    >
      <template #content="{ close }">
        <div class="flex flex-col">
          <div class="flex shrink-0 items-center justify-between gap-4 border-b border-(--ui-border) px-4 py-3">
            <div class="flex items-center gap-4">
              <div class="flex h-12 w-12 shrink-0 items-center justify-center rounded-full bg-[#f5f3f2]">
                <UIcon
                  name="i-lucide-book-open"
                  class="text-lg text-highlighted"
                />
              </div>
              <div>
                <h2 class="font-display text-[20px] font-semibold leading-tight text-highlighted">
                  {{ editingBook ? 'Edit Book' : 'Add Book' }}
                </h2>
                <p class="text-xs font-medium text-muted">
                  {{ editingBook ? 'Update book details' : 'Add a new book to your library' }}
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

          <div class="grow overflow-y-auto py-2 px-3">
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
              ref="bookForm"
              :state="form"
              :validate="validateBook"
              class="flex flex-col gap-6"
              @submit="submitForm"
            >
              <div class="flex flex-col gap-2">
                <label
                  for="book-title"
                  class="text-sm font-medium text-highlighted"
                >
                  Title <span class="text-error">*</span>
                </label>
                <UInput
                  id="book-title"
                  v-model="form.title"
                  placeholder="e.g. Clean Code"
                  :ui="fieldUi"
                />
              </div>

              <div class="flex flex-col gap-2">
                <label
                  for="book-description"
                  class="text-sm font-medium text-highlighted"
                >
                  Description <span class="text-error">*</span>
                </label>
                <UTextarea
                  id="book-description"
                  v-model="form.description"
                  placeholder="Brief summary of the book (at least 10 characters)"
                  :rows="3"
                  :ui="fieldUi"
                />
              </div>

              <div class="grid grid-cols-2 gap-4">
                <div class="flex flex-col gap-2">
                  <label
                    for="book-category"
                    class="text-sm font-medium text-highlighted"
                  >
                    Category <span class="text-error">*</span>
                  </label>
                  <USelectMenu
                    id="book-category"
                    v-model="form.categoryId"
                    :items="categoryOptions"
                    value-key="value"
                    :search-input="false"
                    :ui="fieldUi"
                    icon="i-lucide-tag"
                    placeholder="Select category"
                  />
                </div>

                <div class="flex flex-col gap-2">
                  <label
                    for="book-copies"
                    class="text-sm font-medium text-highlighted"
                  >
                    Copies <span class="text-error">*</span>
                  </label>
                  <UInput
                    id="book-copies"
                    v-model="form.availableCopies"
                    type="number"
                    min="1"
                    :ui="copiesUi"
                  >
                    <template #trailing>
                      <div class="flex h-full flex-col border-l border-(--ui-border)">
                        <button
                          type="button"
                          class="flex flex-1 items-center justify-center px-2 text-muted transition-colors hover:bg-[#f5f3f2]"
                          aria-label="Increase copies"
                          @click="incrementCopies"
                        >
                          <UIcon
                            name="i-lucide-chevron-up"
                            class="size-3.5"
                          />
                        </button>
                        <button
                          type="button"
                          class="flex flex-1 items-center justify-center border-t border-(--ui-border) px-2 text-muted transition-colors hover:bg-[#f5f3f2]"
                          aria-label="Decrease copies"
                          @click="decrementCopies"
                        >
                          <UIcon
                            name="i-lucide-chevron-down"
                            class="size-3.5"
                          />
                        </button>
                      </div>
                    </template>
                  </UInput>
                </div>
              </div>

              <div class="flex flex-col gap-2">
                <label
                  for="book-authors"
                  class="text-sm font-medium text-highlighted"
                >
                  Authors <span class="text-error">*</span>
                </label>
                <USelectMenu
                  id="book-authors"
                  v-model="form.authorIds"
                  :items="authorOptions"
                  value-key="value"
                  multiple
                  search-input
                  :ui="fieldUi"
                  icon="i-lucide-users"
                  placeholder="Select one or more authors"
                />
              </div>
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
              :icon="editingBook ? 'i-lucide-save' : 'i-lucide-book-open'"
              size="lg"
              class="!rounded-lg !px-8 !py-2.5 !bg-brand-700 hover:!bg-brand-600"
              :loading="saving"
              @click="bookForm?.submit()"
            >
              {{ editingBook ? 'Save Changes' : 'Add Book' }}
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
                  Delete Book?
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
              <span class="font-semibold">"{{ deleteTarget?.title }}"</span>?
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
