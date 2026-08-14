<script setup lang="ts">
import {onMounted, ref, type Ref, watch} from 'vue'
import type {ColDef, GetRowIdParams, GridApi, ITextFilterParams} from 'ag-grid-community'
import StatusBadgeCell from '../components/grid/StatusBadgeCell.vue'
import ActionsCell from '../components/grid/ActionsCell.vue'
import {type Book, bookCategories} from '../data/books'
import {type BookStatus, createBook, fetchBooks} from '../services/books'
import type {ChipProps, FormError, SelectMenuItem} from '@nuxt/ui'

const rows: Ref<Book[]> = ref([])
const gridApi = ref<GridApi<Book> | null>(null)
const search = ref('')

const statuses: SelectMenuItem[] = [
  {
    label: 'Available',
    chip: {
      color: "success"
    }
  }, {
    label: 'Borrowed',
    chip: {
      color: "info"
    }
  }, {label: 'Overdue', chip: {color: 'error'}}
]

const statusFilter = ref<'All' | BookStatus>('All')
const categoryFilters = ref<string[]>([])

async function loadBooks() {
  rows.value = await fetchBooks()
}

onMounted(loadBooks)

function getRowId(params: GetRowIdParams) {
  return String((params.data as Book).id)
}

const statusFilterParams: ITextFilterParams = {
  maxNumConditions: 3
}

const categoryFilterParams: ITextFilterParams = {
  maxNumConditions: bookCategories.length
}

const columns: ColDef<Book>[] = [
  {
    headerName: '#',
    sortable: false,
    filter: false,
    width: 48,
    valueGetter: (params) => (params.node?.rowIndex ?? 0) + 1,
    cellStyle: {textAlign: 'center'},
    cellClass: 'text-dimmed'
  },
  {
    field: 'title',
    headerName: 'Title',
    flex: 2,
    cellStyle: {fontWeight: 500},
    cellClass: 'text-highlighted'
  },
  {field: 'author', headerName: 'Author', flex: 2},
  {
    field: 'category',
    headerName: 'Category',
    flex: 1,
    filter: 'agTextColumnFilter',
    filterParams: categoryFilterParams
  },
  {
    field: 'status',
    headerName: 'Status',
    flex: 1,
    filter: 'agTextColumnFilter',
    filterParams: statusFilterParams,
    cellRenderer: StatusBadgeCell
  },
  {field: 'copies', headerName: 'Copies', width: 90, sortable: true, filter: false},
  {
    headerName: 'Actions',
    sortable: false,
    filter: false,
    width: 104,
    pinned: 'right',
    headerClass: 'ag-right-aligned-header',
    cellRenderer: ActionsCell
  }
]

function applyFilters() {
  const api = gridApi.value
  if (!api) return

  const statusModel = statusFilter.value === 'All'
    ? null
    : {filterType: 'text', type: 'equals', filter: statusFilter.value}

  api.setColumnFilterModel('status', statusModel)

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

watch([statusFilter, categoryFilters], applyFilters)

function resetFilters() {
  statusFilter.value = 'All'
  categoryFilters.value = []
}

const addModalOpen = ref(false)
const saving = ref(false)
const addBookForm = ref<{submit: () => Promise<void>} | null>(null)

const newBook = ref<{
  title: string
  author: string
  category: string
  status: BookStatus
  copies: number
}>({
  title: '',
  author: '',
  category: bookCategories[0],
  status: statuses[0],
  copies: 1
})

const statusDotClass: Record<BookStatus, string> = {
  Available: 'success',
  Borrowed: 'info',
  Overdue: 'error'
}

const fieldUi = {
  base: '!rounded-lg !bg-white !py-3 !text-sm !ring-(--ui-border) !placeholder:text-muted focus-visible:!ring-2 focus-visible:!ring-primary focus-visible:!outline-none'
}

const copiesUi = {
  ...fieldUi,
  trailing: '!pe-0',
  base: `${fieldUi.base} [appearance:textfield] [&::-webkit-inner-spin-button]:appearance-none [&::-webkit-outer-spin-button]:appearance-none`
}

function incrementCopies() {
  newBook.value.copies = Math.max(1, (Number(newBook.value.copies) || 1) + 1)
}

function decrementCopies() {
  newBook.value.copies = Math.max(1, (Number(newBook.value.copies) || 1) - 1)
}

function validateBook(state: typeof newBook.value): FormError[] {
  const errors: FormError[] = []
  if (!state.title.trim()) {
    errors.push({name: 'title', message: 'Title is required'})
  }
  if (!state.author.trim()) {
    errors.push({name: 'author', message: 'Author is required'})
  }
  return errors
}

async function submitAddBook() {
  saving.value = true
  try {
    await createBook({...newBook.value, copies: Number(newBook.value.copies) || 1})
    await loadBooks()
    addModalOpen.value = false
    newBook.value = {
      title: '',
      author: '',
      category: bookCategories[0],
      status: 'Available',
      copies: 1
    }
  } finally {
    saving.value = false
  }
}
</script>

<template>
  <div class="flex flex-col">
    <div class="mb-8">
      <div class="mb-2 flex items-center gap-2 text-[13px]">
        <span class="cursor-pointer text-primary hover:underline">
          Library
        </span>
        <UIcon
          name="i-lucide-chevron-right"
          class="size-3.5 text-muted"
        />
        <span class="text-muted">
          Books
        </span>
      </div>

      <h1 class="font-display text-[28px] font-semibold leading-9 tracking-tight text-highlighted">
        Books
      </h1>
      <p class="mt-1 text-sm text-muted">
        Manage and organize all library books.
      </p>
    </div>

    <div class="mb-6 flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
      <div class="relative w-full md:w-[400px]">
        <UIcon
          name="i-lucide-search"
          class="pointer-events-none absolute left-3 top-1/2 size-[18px] -translate-y-1/2 text-muted"
        />
        <input
          v-model="search"
          type="text"
          placeholder="Search books by title, author or ISBN..."
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
                  Status
                </p>
                <URadioGroup
                  v-model="statusFilter"
                  orientation="horizontal"
                  size="xs"
                  :ui="{fieldset:'flex-wrap'}"
                  :items="['All', ...statuses]"
                  variant="card"
                />
              </div>

              <div class="flex flex-col gap-2">
                <p class="text-[11px] font-semibold uppercase tracking-wide text-muted">
                  Category
                </p>
                <UCheckboxGroup
                  v-model="categoryFilters"
                  size="sm"
                  variant="list"
                  :items="bookCategories"
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
          @click="addModalOpen = true"
        >
          Add Book
        </UButton>
      </div>
    </div>

    <div class="overflow-hidden rounded-xl bg-white shadow-sm ring-1 ring-(--ui-border)">
      <AppDataGrid
        v-model:api="gridApi"
        :rows="rows"
        :columns="columns"
        :quick-filter-text="search"
        :get-row-id="getRowId"
        height="440px"
      />
    </div>

    <UModal
      v-model:open="addModalOpen"
      title="Add Book"
      description="Add a new book to your library"
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
                  Add Book
                </h2>
                <p class="text-xs font-medium text-muted">
                  Add a new book to your library
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
            <UForm
              ref="addBookForm"
              :state="newBook"
              :validate="validateBook"
              class="flex flex-col gap-6"
              @submit="submitAddBook"
            >
              <div class="flex flex-col gap-2">
                <label
                  for="add-book-title"
                  class="text-sm font-medium text-highlighted"
                >
                  Title <span class="text-error">*</span>
                </label>
                <UInput
                  id="add-book-title"
                  v-model="newBook.title"
                  placeholder="e.g. Clean Code"
                  :ui="fieldUi"
                />
              </div>

              <div class="flex flex-col gap-2">
                <label
                  for="add-book-author"
                  class="text-sm font-medium text-highlighted"
                >
                  Author <span class="text-error">*</span>
                </label>
                <UInput
                  id="add-book-author"
                  v-model="newBook.author"
                  placeholder="e.g. Robert C. Martin"
                  :ui="fieldUi"
                />
              </div>

              <div class="grid grid-cols-2 gap-4">
                <div class="flex flex-col gap-2">
                  <label
                    for="add-book-category"
                    class="text-sm font-medium text-highlighted"
                  >
                    Category
                  </label>
                  <USelectMenu
                    id="add-book-category"
                    v-model="newBook.category"
                    :items="bookCategories"
                    size="md"
                    :search-input="false"
                    :ui="fieldUi"
                    icon="i-lucide-tag"
                  >
                  </USelectMenu>
                </div>

                <div class="flex flex-col gap-2">
                  <label
                    for="add-book-status"
                    class="text-sm font-medium text-highlighted"
                  >
                    Status
                  </label>
                  <USelectMenu
                    id="add-book-status"
                    v-model="newBook.status"
                    :items="statuses"
                    size="md"
                    :search-input="false"
                    :ui="fieldUi"
                  >
                    <template #leading="{ modelValue, ui }">
                      <UChip
                        v-if="modelValue"
                        v-bind="modelValue.chip"
                        inset
                        standalone
                        :size="(ui.itemLeadingChipSize() as ChipProps['size'])"
                        :class="ui.itemLeadingChip()"
                      />
                    </template>
                  </USelectMenu>
                </div>
              </div>

              <div class="flex flex-col gap-2">
                <div class="flex items-center gap-1">
                  <label
                    for="add-book-copies"
                    class="text-sm font-medium text-highlighted"
                  >
                    Copies
                  </label>
                  <UIcon
                    name="i-lucide-circle-help"
                    class="size-3.5 text-muted"
                  />
                </div>
                <UInput
                  id="add-book-copies"
                  v-model="newBook.copies"
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
              icon="i-lucide-book-open"
              size="lg"
              class="!rounded-lg !px-8 !py-2.5 !bg-brand-700 hover:!bg-brand-600"
              :loading="saving"
              @click="addBookForm?.submit()"
            >
              Add Book
            </UButton>
          </div>
        </div>
      </template>
    </UModal>
  </div>
</template>
