<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import Modal from '../components/Modal.vue'
import ConfirmDialog from '../components/ConfirmDialog.vue'
import { bookApi } from '../services/books'
import { authorApi } from '../services/authors'
import { categoryApi } from '../services/categories'
import { getErrorMessage } from '../services/http'
import type { Author, Book, Category } from '../types'

const books = ref<Book[]>([])
const categories = ref<Category[]>([])
const authors = ref<Author[]>([])
const loading = ref(true)
const error = ref('')

const formOpen = ref(false)
const editingId = ref<number | null>(null)
const submitting = ref(false)
const formError = ref('')
const form = reactive({
  title: '',
  description: '',
  availableCopies: 0,
  categoryId: null as number | null,
  authorIds: [] as number[],
})

const confirmOpen = ref(false)
const deletingId = ref<number | null>(null)
const deletingTitle = ref('')
const deleting = ref(false)

function formatAuthors(authors: Author[]): string {
  return authors.map((a) => a.name).join(', ')
}

function selectAuthor(id: number) {
  const index = form.authorIds.indexOf(id)
  if (index === -1) {
    form.authorIds.push(id)
  } else {
    form.authorIds.splice(index, 1)
  }
}

async function loadBooks() {
  loading.value = true
  error.value = ''
  try {
    const { data } = await bookApi.getAll()
    books.value = data.data
  } catch (err) {
    error.value = getErrorMessage(err)
  } finally {
    loading.value = false
  }
}

async function loadFormOptions() {
  const [categoryRes, authorRes] = await Promise.all([categoryApi.getAll(), authorApi.getAll()])
  categories.value = categoryRes.data.data
  authors.value = authorRes.data.data
}

function openCreate() {
  editingId.value = null
  form.title = ''
  form.description = ''
  form.availableCopies = 0
  form.categoryId = null
  form.authorIds = []
  formError.value = ''
  formOpen.value = true
  loadFormOptions()
}

function openEdit(book: Book) {
  editingId.value = book.id
  form.title = book.title
  form.description = book.description
  form.availableCopies = book.availableCopies
  form.categoryId = book.category.id
  form.authorIds = book.authors.map((a) => a.id)
  formError.value = ''
  formOpen.value = true
  loadFormOptions()
}

async function submitForm() {
  formError.value = ''
  if (form.categoryId === null) {
    formError.value = 'Please choose a category.'
    return
  }
  submitting.value = true
  try {
    const payload = {
      title: form.title.trim(),
      description: form.description.trim(),
      availableCopies: form.availableCopies,
      categoryId: form.categoryId,
      authorIds: form.authorIds,
    }
    if (editingId.value === null) {
      await bookApi.create(payload)
    } else {
      await bookApi.update(editingId.value, payload)
    }
    formOpen.value = false
    await loadBooks()
  } catch (err) {
    formError.value = getErrorMessage(err)
  } finally {
    submitting.value = false
  }
}

function askDelete(book: Book) {
  deletingId.value = book.id
  deletingTitle.value = book.title
  confirmOpen.value = true
}

async function confirmDelete() {
  if (deletingId.value === null) return
  deleting.value = true
  try {
    await bookApi.remove(deletingId.value)
    confirmOpen.value = false
    await loadBooks()
  } catch (err) {
    error.value = getErrorMessage(err)
    confirmOpen.value = false
  } finally {
    deleting.value = false
  }
}

onMounted(loadBooks)
</script>

<template>
  <div>
    <div class="mb-6 flex items-center justify-between">
      <div>
        <h1 class="text-xl font-semibold text-gray-900">Books</h1>
        <p class="mt-1 text-sm text-gray-500">Browse and manage the library catalog.</p>
      </div>
      <button
        class="flex items-center gap-2 rounded-lg bg-indigo-600 px-4 py-2 text-sm font-medium text-white shadow-sm transition-colors hover:bg-indigo-700"
        @click="openCreate"
      >
        <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
        </svg>
        Add book
      </button>
    </div>

    <div v-if="error" class="mb-4 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
      {{ error }}
    </div>

    <div v-if="loading" class="animate-pulse space-y-3">
      <div v-for="i in 5" :key="i" class="h-16 rounded-lg bg-gray-200" />
    </div>

    <div v-else class="overflow-hidden rounded-xl border border-gray-200 bg-white shadow-sm">
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-6 py-3 text-left text-xs font-semibold uppercase tracking-wide text-gray-500">Title</th>
            <th class="px-6 py-3 text-left text-xs font-semibold uppercase tracking-wide text-gray-500">Category</th>
            <th class="px-6 py-3 text-left text-xs font-semibold uppercase tracking-wide text-gray-500">Authors</th>
            <th class="px-6 py-3 text-left text-xs font-semibold uppercase tracking-wide text-gray-500">Copies</th>
            <th class="px-6 py-3 text-right text-xs font-semibold uppercase tracking-wide text-gray-500">Actions</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-200">
          <tr v-for="book in books" :key="book.id" class="hover:bg-gray-50">
            <td class="px-6 py-4">
              <p class="text-sm font-medium text-gray-900">{{ book.title }}</p>
              <p class="mt-0.5 max-w-xs truncate text-xs text-gray-500">{{ book.description }}</p>
            </td>
            <td class="px-6 py-4">
              <span
                class="inline-flex items-center rounded-full bg-indigo-50 px-2.5 py-0.5 text-xs font-medium text-indigo-700"
              >
                {{ book.category.name }}
              </span>
            </td>
            <td class="px-6 py-4 text-sm text-gray-600">{{ formatAuthors(book.authors) || '—' }}</td>
            <td class="px-6 py-4 text-sm text-gray-600">{{ book.availableCopies }}</td>
            <td class="px-6 py-4">
              <div class="flex justify-end gap-2">
                <button
                  class="rounded-lg p-2 text-gray-500 transition-colors hover:bg-gray-100 hover:text-indigo-600"
                  title="Edit"
                  @click="openEdit(book)"
                >
                  <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke-width="1.8" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M16.862 4.487l1.687-1.688a1.875 1.875 0 112.652 2.652L6.832 19.82a4.5 4.5 0 01-1.897 1.13l-2.685.8.8-2.685a4.5 4.5 0 011.13-1.897L16.862 4.487z" />
                  </svg>
                </button>
                <button
                  class="rounded-lg p-2 text-gray-500 transition-colors hover:bg-red-50 hover:text-red-600"
                  title="Delete"
                  @click="askDelete(book)"
                >
                  <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke-width="1.8" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M14.74 9l-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 01-2.244 2.077H8.084a2.25 2.25 0 01-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 00-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 013.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 00-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 00-7.5 0" />
                  </svg>
                </button>
              </div>
            </td>
          </tr>
          <tr v-if="books.length === 0">
            <td colspan="5" class="px-6 py-10 text-center text-sm text-gray-500">No books yet. Add your first one.</td>
          </tr>
        </tbody>
      </table>
    </div>

    <Modal
      :open="formOpen"
      :title="editingId === null ? 'Add book' : 'Edit book'"
      @close="formOpen = false"
    >
      <form @submit.prevent="submitForm" class="space-y-4">
        <div v-if="formError" class="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
          {{ formError }}
        </div>

        <div>
          <label class="mb-1.5 block text-sm font-medium text-gray-700" for="book-title">Title</label>
          <input
            id="book-title"
            v-model.trim="form.title"
            type="text"
            required
            placeholder="Book title"
            class="w-full rounded-lg border border-gray-300 px-3.5 py-2.5 text-sm shadow-sm outline-none transition-colors focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200"
          />
        </div>

        <div>
          <label class="mb-1.5 block text-sm font-medium text-gray-700" for="book-description">Description</label>
          <textarea
            id="book-description"
            v-model.trim="form.description"
            rows="3"
            placeholder="Short description (min 10 characters)"
            class="w-full resize-none rounded-lg border border-gray-300 px-3.5 py-2.5 text-sm shadow-sm outline-none transition-colors focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200"
          />
        </div>

        <div class="grid grid-cols-2 gap-4">
          <div>
            <label class="mb-1.5 block text-sm font-medium text-gray-700" for="book-copies">Available copies</label>
            <input
              id="book-copies"
              v-model.number="form.availableCopies"
              type="number"
              min="0"
              required
              class="w-full rounded-lg border border-gray-300 px-3.5 py-2.5 text-sm shadow-sm outline-none transition-colors focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200"
            />
          </div>
          <div>
            <label class="mb-1.5 block text-sm font-medium text-gray-700" for="book-category">Category</label>
            <select
              id="book-category"
              v-model.number="form.categoryId"
              class="w-full rounded-lg border border-gray-300 px-3.5 py-2.5 text-sm shadow-sm outline-none transition-colors focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200"
            >
              <option :value="null" disabled>Choose a category</option>
              <option v-for="category in categories" :key="category.id" :value="category.id">
                {{ category.name }}
              </option>
            </select>
          </div>
        </div>

        <div>
          <span class="mb-1.5 block text-sm font-medium text-gray-700">Authors</span>
          <div v-if="authors.length === 0" class="text-sm text-gray-500">
            No authors available. Add authors first.
          </div>
          <div v-else class="flex flex-wrap gap-2">
            <button
              v-for="author in authors"
              :key="author.id"
              type="button"
              class="rounded-full border px-3 py-1.5 text-sm font-medium transition-colors"
              :class="
                form.authorIds.includes(author.id)
                  ? 'border-indigo-600 bg-indigo-600 text-white'
                  : 'border-gray-300 text-gray-700 hover:border-indigo-400 hover:text-indigo-600'
              "
              @click="selectAuthor(author.id)"
            >
              {{ author.name }}
            </button>
          </div>
        </div>

        <div class="flex justify-end gap-3 pt-2">
          <button
            type="button"
            class="rounded-lg border border-gray-300 px-4 py-2 text-sm font-medium text-gray-700 transition-colors hover:bg-gray-50"
            @click="formOpen = false"
          >
            Cancel
          </button>
          <button
            type="submit"
            :disabled="submitting"
            class="rounded-lg bg-indigo-600 px-4 py-2 text-sm font-medium text-white transition-colors hover:bg-indigo-700 disabled:opacity-50"
          >
            {{ submitting ? 'Saving...' : 'Save' }}
          </button>
        </div>
      </form>
    </Modal>

    <ConfirmDialog
      :open="confirmOpen"
      :busy="deleting"
      message="Are you sure you want to delete this book?"
      @close="confirmOpen = false"
      @confirm="confirmDelete"
    >
      <p class="mt-1 text-sm font-medium text-gray-900">{{ deletingTitle }}</p>
    </ConfirmDialog>
  </div>
</template>
