<script setup lang="ts">
import { computed, onMounted, ref, type Ref } from 'vue'
import type { ColDef, GetRowIdParams, GridApi } from 'ag-grid-community'
import type { FormError, SelectMenuItem } from '@nuxt/ui'
import BookCell from '../components/grid/BookCell.vue'
import MemberCell from '../components/grid/MemberCell.vue'
import StatusCell from '../components/grid/StatusCell.vue'
import StatusBadge from '../components/grid/StatusBadge.vue'
import DueDateCell from '../components/grid/DueDateCell.vue'
import BorrowingActionsCell from '../components/grid/BorrowingActionsCell.vue'
import {
  BORROWING_STATUSES,
  borrowBook,
  fetchBorrowings,
  formatDate,
  getStatus,
  todayStr,
  type Borrowing,
  type BorrowingStatus
} from '../services/borrowings'
import { fetchBooks, type Book } from '../services/books'
import { ApiError } from '../services/api'

const rows: Ref<Borrowing[]> = ref([])
const books: Ref<Book[]> = ref([])
const gridApi = ref<GridApi<Borrowing> | null>(null)
const search = ref('')
const loadError = ref('')

const statusFilters = ref<BorrowingStatus[]>([])
// eslint-disable-next-line @typescript-eslint/no-explicit-any
const dateRange = ref<any>(null)

const today = todayStr()

async function loadAll() {
  loadError.value = ''
  try {
    const [borrowings, bookList] = await Promise.all([fetchBorrowings(), fetchBooks()])
    rows.value = borrowings
    books.value = bookList
  } catch (e) {
    loadError.value = e instanceof ApiError ? e.message : 'Failed to load borrowings.'
  }
}

onMounted(loadAll)

function getRowId(params: GetRowIdParams) {
  return String((params.data as Borrowing).id)
}

const columns: ColDef<Borrowing>[] = [
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
    headerName: 'Book',
    flex: 2,
    minWidth: 200,
    valueGetter: (params) =>
      `${(params.data as Borrowing).book.title} ${(params.data as Borrowing).book.authors.map((author) => author.name).join(', ')}`,
    cellRenderer: BookCell
  },
  {
    headerName: 'Member',
    flex: 2,
    minWidth: 200,
    valueGetter: (params) =>
      `${(params.data as Borrowing).user.name} ${(params.data as Borrowing).user.email}`,
    cellRenderer: MemberCell
  },
  {
    headerName: 'Status',
    width: 120,
    valueGetter: (params) => getStatus(params.data as Borrowing),
    cellRenderer: StatusCell,
    filter: 'agTextColumnFilter'
  },
  {
    headerName: 'Borrow Date',
    width: 130,
    valueGetter: (params) => (params.data as Borrowing).borrowDate,
    valueFormatter: (params) => formatDate(params.value as string)
  },
  {
    headerName: 'Due Date',
    width: 160,
    valueGetter: (params) => (params.data as Borrowing).dueDate,
    cellRenderer: DueDateCell
  },
  {
    headerName: 'Return Date',
    width: 130,
    valueGetter: (params) => (params.data as Borrowing).returnDate,
    valueFormatter: (params) => formatDate(params.value as string | null),
    cellClass: (params) => (params.value ? '' : 'text-muted')
  },
  {
    headerName: 'Actions',
    sortable: false,
    filter: false,
    width: 88,
    pinned: 'right',
    headerClass: 'ag-right-aligned-header',
    cellRenderer: BorrowingActionsCell,
    cellRendererParams: {
      onView: (borrowing: Borrowing) => openDetail(borrowing)
    }
  }
]

const totalBorrowings = computed(() => rows.value.length)
const currentlyBorrowed = computed(() => rows.value.filter((borrowing) => !borrowing.returnDate).length)
const dueToday = computed(() => rows.value.filter((borrowing) => !borrowing.returnDate && borrowing.dueDate === today).length)
const overdue = computed(() => rows.value.filter((borrowing) => !borrowing.returnDate && borrowing.dueDate < today).length)

const ALL_STATUS = '__all__'

const statusOptions = computed<SelectMenuItem[]>(() => [
  { label: 'All Status', value: ALL_STATUS },
  ...BORROWING_STATUSES.map((status) => ({ label: status, value: status }))
])

const statusDropdown = computed<string>({
  get: () => (statusFilters.value.length === 1 ? statusFilters.value[0] : ALL_STATUS),
  set: (value) => {
    statusFilters.value = value && value !== ALL_STATUS ? [value as BorrowingStatus] : []
  }
})

const visibleRows = computed(() => {
  const range = dateRange.value
  return rows.value.filter((borrowing) => {
    if (statusFilters.value.length > 0 && !statusFilters.value.includes(getStatus(borrowing))) {
      return false
    }
    if (range?.start && range?.end) {
      const start = range.start.toString()
      const end = range.end.toString()
      if (borrowing.borrowDate < start || borrowing.borrowDate > end) return false
    }
    return true
  })
})

const rangeLabel = computed(() => {
  const range = dateRange.value
  if (!range?.start || !range?.end) return 'Select date range'
  return `${formatDate(range.start.toString())} – ${formatDate(range.end.toString())}`
})

function resetFilters() {
  statusFilters.value = []
  dateRange.value = null
}

const addOpen = ref(false)
const selectedBookId = ref<number | null>(null)
const saving = ref(false)
const formError = ref('')
const borrowForm = ref<{ submit: () => Promise<void> } | null>(null)

const bookOptions = computed<SelectMenuItem[]>(() =>
  books.value
    .filter((book) => book.availableCopies > 0)
    .sort((a, b) => a.title.localeCompare(b.title))
    .map((book) => ({ label: book.title, value: book.id }))
)

const borrowState = computed(() => ({ bookId: selectedBookId.value }))

function openAdd() {
  selectedBookId.value = null
  formError.value = ''
  addOpen.value = true
}

function validateBorrow(state: { bookId: number | null }): FormError[] {
  const errors: FormError[] = []
  if (state.bookId == null) {
    errors.push({ name: 'bookId', message: 'Select a book to borrow' })
  }
  return errors
}

async function submitBorrow() {
  saving.value = true
  formError.value = ''
  try {
    await borrowBook(selectedBookId.value as number)
    await loadAll()
    addOpen.value = false
  } catch (e) {
    formError.value = e instanceof ApiError ? e.message : 'Failed to borrow book.'
  } finally {
    saving.value = false
  }
}

const detailTarget: Ref<Borrowing | null> = ref(null)
const detail = computed(() => detailTarget.value)

const detailOpen = computed({
  get: () => detailTarget.value !== null,
  set: (value) => {
    if (!value) detailTarget.value = null
  }
})

function openDetail(borrowing: Borrowing) {
  detailTarget.value = borrowing
}

const toolbarUi = {
  base: '!h-[38px] !rounded-lg !bg-(--ui-bg-card) !text-sm !ring-(--ui-border) shadow-sm focus-visible:!ring-2 focus-visible:!ring-primary focus-visible:!outline-none'
}

const fieldUi = {
  base: '!rounded-lg !bg-(--ui-bg-card) !py-3 !text-sm !ring-(--ui-border) !placeholder:text-muted focus-visible:!ring-2 focus-visible:!ring-primary focus-visible:!outline-none'
}

const modalUi = {
  overlay: '!bg-black/20 backdrop-blur-sm',
  content: '!max-w-lg !rounded-xl !bg-(--ui-bg-card) !shadow-[0_8px_32px_rgba(0,0,0,0.04)] !ring-0 border border-(--ui-border)'
}

const detailStatus = computed<BorrowingStatus>(() => (detail.value ? getStatus(detail.value) : 'Borrowed'))

</script>

<template>
  <div class="flex min-h-0 flex-1 flex-col">
    <div class="mb-3">
<!--      <UBreadcrumb :items="items" />-->

      <div class="flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
        <div>
          <h1 class="font-display text-[28px] font-semibold leading-9 tracking-tight text-highlighted">
            Borrowings
          </h1>
          <p class="mt-1 text-sm text-muted">
            Track and manage all book borrowings.
          </p>
        </div>

        <UButton
          icon="i-lucide-plus"
          class="!h-[38px] !rounded-lg !px-4 shadow-sm"
          @click="openAdd"
        >
          New Borrowing
        </UButton>
      </div>
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

    <div class="hidden mb-4 grid grid-cols-1 gap-3 sm:grid-cols-2 lg:grid-cols-4">
      <div class="flex items-center gap-4 rounded-xl bg-(--ui-bg-card) p-4 shadow-sm ring-1 ring-(--ui-border)">
        <div class="flex h-12 w-12 shrink-0 items-center justify-center rounded-lg bg-green-100 text-green-700 dark:bg-green-950/50 dark:text-green-400">
          <UIcon
            name="i-lucide-book-marked"
            class="size-5"
          />
        </div>
        <div class="min-w-0">
          <p class="text-xs font-medium text-muted">
            Total Borrowings
          </p>
          <p class="font-display text-[24px] font-semibold leading-7 text-highlighted">
            {{ totalBorrowings }}
          </p>
          <p class="text-[10px] text-muted">
            All time
          </p>
        </div>
      </div>

      <div class="flex items-center gap-4 rounded-xl bg-(--ui-bg-card) p-4 shadow-sm ring-1 ring-(--ui-border)">
        <div class="flex h-12 w-12 shrink-0 items-center justify-center rounded-lg bg-amber-100 text-amber-700 dark:bg-amber-950/50 dark:text-amber-400">
          <UIcon
            name="i-lucide-hourglass"
            class="size-5"
          />
        </div>
        <div class="min-w-0">
          <p class="text-xs font-medium text-muted">
            Currently Borrowed
          </p>
          <p class="font-display text-[24px] font-semibold leading-7 text-highlighted">
            {{ currentlyBorrowed }}
          </p>
          <p class="text-[10px] text-muted">
            Books out
          </p>
        </div>
      </div>

      <div class="flex items-center gap-4 rounded-xl bg-(--ui-bg-card) p-4 shadow-sm ring-1 ring-(--ui-border)">
        <div class="flex h-12 w-12 shrink-0 items-center justify-center rounded-lg bg-blue-100 text-blue-700 dark:bg-blue-950/50 dark:text-blue-400">
          <UIcon
            name="i-lucide-calendar-days"
            class="size-5"
          />
        </div>
        <div class="min-w-0">
          <p class="text-xs font-medium text-muted">
            Due Today
          </p>
          <p class="font-display text-[24px] font-semibold leading-7 text-primary">
            {{ dueToday }}
          </p>
          <p class="text-[10px] text-muted">
            Due today
          </p>
        </div>
      </div>

      <div class="flex items-center gap-4 rounded-xl bg-(--ui-bg-card) p-4 shadow-sm ring-1 ring-(--ui-border)">
        <div class="flex h-12 w-12 shrink-0 items-center justify-center rounded-lg bg-red-100 text-red-700 dark:bg-red-950/50 dark:text-red-400">
          <UIcon
            name="i-lucide-triangle-alert"
            class="size-5"
          />
        </div>
        <div class="min-w-0">
          <p class="text-xs font-medium text-muted">
            Overdue
          </p>
          <p class="font-display text-[24px] font-semibold leading-7 text-error">
            {{ overdue }}
          </p>
          <p class="text-[10px] text-muted">
            Past due date
          </p>
        </div>
      </div>
    </div>

    <div class="flex min-h-0 flex-1 flex-col overflow-hidden rounded-xl bg-(--ui-bg-card) shadow-sm ring-1 ring-(--ui-border)">
      <div class="flex flex-col gap-2 border-b border-(--ui-border) p-2 lg:flex-row lg:items-end lg:overflow-x-auto">
        <div class="relative w-full lg:w-auto lg:min-w-[220px] lg:flex-1">
          <UIcon
            name="i-lucide-search"
            class="pointer-events-none absolute left-3 top-1/2 size-[18px] -translate-y-1/2 text-muted"
          />
          <input
            v-model="search"
            type="text"
            placeholder="Search by book title or member name..."
            class="h-[38px] w-full rounded-lg border border-(--ui-border) bg-(--ui-bg-card) pl-9 pr-4 text-sm text-highlighted shadow-sm outline-none transition-colors placeholder:text-muted focus:border-primary focus:ring-1 focus:ring-primary"
          >
        </div>

        <div class="flex flex-col gap-1">
          <USelectMenu
            id="borrow-status"
            v-model="statusDropdown"
            :items="statusOptions"
            value-key="value"
            :search-input="false"
            class="w-full lg:w-44"
            :ui="toolbarUi"
          />
        </div>

        <div class="flex flex-col gap-1">
          <UPopover>
            <button
              type="button"
              class="flex h-[38px] w-full items-center justify-between gap-2 rounded-lg border border-(--ui-border) bg-(--ui-bg-card) px-3 text-sm shadow-sm outline-none transition-colors focus:border-primary focus:ring-1 focus:ring-primary lg:w-64"
            >
              <span
                class="truncate"
                :class="dateRange ? 'text-highlighted' : 'text-muted'"
              >
                {{ rangeLabel }}
              </span>
              <UIcon
                name="i-lucide-calendar"
                class="size-4 shrink-0 text-muted"
              />
            </button>

            <template #content>
              <UCalendar
                v-model="dateRange"
                range
              />
            </template>
          </UPopover>
        </div>

        <UPopover>
          <UButton
            color="neutral"
            variant="outline"
            icon="i-lucide-filter"
            class="!h-[38px] !rounded-lg !bg-(--ui-bg-card) !px-4 shadow-sm"
          >
            Filters
          </UButton>

          <template #content>
            <div class="flex w-64 flex-col gap-5 p-4">
              <div class="flex flex-col gap-2">
                <p class="text-[11px] font-semibold uppercase tracking-wide text-muted">
                  Status
                </p>
                <UCheckboxGroup
                  v-model="statusFilters"
                  size="sm"
                  variant="list"
                  :items="[...BORROWING_STATUSES]"
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
      </div>

      <AppDataGrid
        v-model:api="gridApi"
        :rows="visibleRows"
        :columns="columns"
        :quick-filter-text="search"
        :get-row-id="getRowId"
        :row-height="52"
        height="100%"
      />
    </div>

    <UModal
      v-model:open="addOpen"
      :title="'New Borrowing'"
      :description="'Borrow a book to your account'"
      :ui="modalUi"
    >
      <template #content="{ close }">
        <div class="flex flex-col">
          <div class="flex shrink-0 items-center justify-between gap-4 border-b border-(--ui-border) px-4 py-3">
            <div class="flex items-center gap-4">
              <div class="flex h-12 w-12 shrink-0 items-center justify-center rounded-full bg-(--ui-bg-accented)">
                <UIcon
                  name="i-lucide-book-open"
                  class="text-lg text-highlighted"
                />
              </div>
              <div>
                <h2 class="font-display text-[20px] font-semibold leading-tight text-highlighted">
                  New Borrowing
                </h2>
                <p class="text-xs font-medium text-muted">
                  Borrow a book to your account
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
              class="mb-4 flex items-center gap-2 rounded-lg border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-700 dark:border-red-950 dark:bg-red-950/40 dark:text-red-300"
            >
              <UIcon
                name="i-lucide-alert-circle"
                class="size-4 shrink-0"
              />
              <span>{{ formError }}</span>
            </div>

            <UForm
              ref="borrowForm"
              :state="borrowState"
              :validate="validateBorrow"
              class="flex flex-col gap-6"
              @submit="submitBorrow"
            >
              <div class="flex flex-col gap-2">
                <label
                  for="borrow-book"
                  class="text-sm font-medium text-highlighted"
                >
                  Book <span class="text-error">*</span>
                </label>
                <USelectMenu
                  id="borrow-book"
                  v-model="selectedBookId"
                  :items="bookOptions"
                  value-key="value"
                  :search-input="true"
                  :ui="fieldUi"
                  icon="i-lucide-book-open"
                  placeholder="Select book to borrow"
                />
              </div>
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
              icon="i-lucide-book-open"
              size="lg"
              class="!rounded-lg !px-8 !py-2.5 !bg-brand-700 dark:!bg-primary-400 hover:!bg-brand-600 dark:hover:!bg-primary-300"
              :loading="saving"
              @click="borrowForm?.submit()"
            >
              Borrow Book
            </UButton>
          </div>
        </div>
      </template>
    </UModal>

    <UModal
      v-model:open="detailOpen"
      :ui="{
        overlay: '!bg-black/20 backdrop-blur-sm',
        content: '!max-w-lg !rounded-xl !bg-(--ui-bg-card) !shadow-[0_8px_32px_rgba(0,0,0,0.04)] !ring-0 border border-(--ui-border)'
      }"
    >
      <template #content>
        <div class="flex flex-col">
          <div class="flex shrink-0 items-center justify-between gap-4 border-b border-(--ui-border) px-4 py-3">
            <div class="flex items-center gap-4">
              <div class="flex h-12 w-12 shrink-0 items-center justify-center rounded-full bg-(--ui-bg-accented)">
                <UIcon
                  name="i-lucide-book-open"
                  class="text-lg text-highlighted"
                />
              </div>
              <div>
                <h2 class="font-display text-[20px] font-semibold leading-tight text-highlighted">
                  Borrowing Details
                </h2>
                <p class="text-xs font-medium text-muted">
                  #{{ detail?.id }}
                </p>
              </div>
            </div>

            <UButton
              color="neutral"
              variant="ghost"
              icon="i-lucide-x"
              aria-label="Close modal"
              @click="detailTarget = null"
            />
          </div>

          <div
            v-if="detail"
            class="px-4 py-4"
          >
            <div class="flex flex-col gap-4">
              <div class="flex items-start justify-between gap-4">
                <div class="flex items-center gap-3">
                  <div class="flex h-9 w-9 shrink-0 items-center justify-center rounded-lg bg-(--ui-bg-accented)">
                    <UIcon
                      name="i-lucide-book-marked"
                      class="size-4 text-highlighted"
                    />
                  </div>
                  <div class="min-w-0">
                    <p class="truncate text-sm font-semibold text-highlighted">
                      {{ detail.book.title }}
                    </p>
                    <p class="truncate text-xs text-muted">
                      {{ detail.book.authors.map((author) => author.name).join(', ') }}
                    </p>
                  </div>
                </div>
                <StatusBadge :status="detailStatus" />
              </div>

              <div class="h-px bg-(--ui-border)" />

              <div class="flex items-start justify-between gap-4">
                <p class="text-xs font-medium uppercase tracking-wide text-muted">
                  Member
                </p>
                <div class="text-right">
                  <p class="text-sm font-medium text-highlighted">
                    {{ detail.user.name }}
                  </p>
                  <p class="text-xs text-muted">
                    {{ detail.user.email }}
                  </p>
                </div>
              </div>

              <div class="grid grid-cols-3 gap-3">
                <div class="flex flex-col gap-1 rounded-lg border border-(--ui-border) bg-(--ui-bg-accented) p-3">
                  <p class="text-[10px] font-semibold uppercase tracking-wide text-muted">
                    Borrow Date
                  </p>
                  <p class="text-sm font-medium text-highlighted">
                    {{ formatDate(detail.borrowDate) }}
                  </p>
                </div>
                <div class="flex flex-col gap-1 rounded-lg border border-(--ui-border) bg-(--ui-bg-accented) p-3">
                  <p class="text-[10px] font-semibold uppercase tracking-wide text-muted">
                    Due Date
                  </p>
                  <p class="text-sm font-medium text-highlighted">
                    {{ formatDate(detail.dueDate) }}
                  </p>
                </div>
                <div class="flex flex-col gap-1 rounded-lg border border-(--ui-border) bg-(--ui-bg-accented) p-3">
                  <p class="text-[10px] font-semibold uppercase tracking-wide text-muted">
                    Return Date
                  </p>
                  <p
                    class="text-sm font-medium"
                    :class="detail.returnDate ? 'text-highlighted' : 'text-muted'"
                  >
                    {{ formatDate(detail.returnDate) }}
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </template>
    </UModal>
  </div>
</template>
