<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import Modal from '../components/Modal.vue'
import ConfirmDialog from '../components/ConfirmDialog.vue'
import { categoryApi } from '../services/categories'
import { getErrorMessage } from '../services/http'
import type { Category } from '../types'

const categories = ref<Category[]>([])
const loading = ref(true)
const error = ref('')

const formOpen = ref(false)
const editingId = ref<number | null>(null)
const submitting = ref(false)
const formError = ref('')
const form = reactive({ name: '' })

const confirmOpen = ref(false)
const deletingId = ref<number | null>(null)
const deletingName = ref('')
const deleting = ref(false)

const sorted = computed(() =>
  [...categories.value].sort((a, b) => a.name.localeCompare(b.name)),
)

async function loadCategories() {
  loading.value = true
  error.value = ''
  try {
    const { data } = await categoryApi.getAll()
    categories.value = data.data
  } catch (err) {
    error.value = getErrorMessage(err)
  } finally {
    loading.value = false
  }
}

function openCreate() {
  editingId.value = null
  form.name = ''
  formError.value = ''
  formOpen.value = true
}

function openEdit(category: Category) {
  editingId.value = category.id
  form.name = category.name
  formError.value = ''
  formOpen.value = true
}

async function submitForm() {
  formError.value = ''
  submitting.value = true
  try {
    const payload = { name: form.name.trim() }
    if (editingId.value === null) {
      await categoryApi.create(payload)
    } else {
      await categoryApi.update(editingId.value, payload)
    }
    formOpen.value = false
    await loadCategories()
  } catch (err) {
    formError.value = getErrorMessage(err)
  } finally {
    submitting.value = false
  }
}

function askDelete(category: Category) {
  deletingId.value = category.id
  deletingName.value = category.name
  confirmOpen.value = true
}

async function confirmDelete() {
  if (deletingId.value === null) return
  deleting.value = true
  try {
    await categoryApi.remove(deletingId.value)
    confirmOpen.value = false
    await loadCategories()
  } catch (err) {
    error.value = getErrorMessage(err)
    confirmOpen.value = false
  } finally {
    deleting.value = false
  }
}

onMounted(loadCategories)
</script>

<template>
  <div>
    <div class="mb-6 flex items-center justify-between">
      <div>
        <h1 class="text-xl font-semibold text-gray-900">Categories</h1>
        <p class="mt-1 text-sm text-gray-500">Organize the library into categories.</p>
      </div>
      <button
        class="flex items-center gap-2 rounded-lg bg-indigo-600 px-4 py-2 text-sm font-medium text-white shadow-sm transition-colors hover:bg-indigo-700"
        @click="openCreate"
      >
        <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
        </svg>
        Add category
      </button>
    </div>

    <div v-if="error" class="mb-4 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
      {{ error }}
    </div>

    <div v-if="loading" class="animate-pulse space-y-3">
      <div v-for="i in 5" :key="i" class="h-12 rounded-lg bg-gray-200" />
    </div>

    <div v-else class="overflow-hidden rounded-xl border border-gray-200 bg-white shadow-sm">
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-6 py-3 text-left text-xs font-semibold uppercase tracking-wide text-gray-500">#</th>
            <th class="px-6 py-3 text-left text-xs font-semibold uppercase tracking-wide text-gray-500">Name</th>
            <th class="px-6 py-3 text-right text-xs font-semibold uppercase tracking-wide text-gray-500">Actions</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-200">
          <tr v-for="(category, index) in sorted" :key="category.id" class="hover:bg-gray-50">
            <td class="px-6 py-4 text-sm text-gray-500">{{ index + 1 }}</td>
            <td class="px-6 py-4 text-sm font-medium text-gray-900">{{ category.name }}</td>
            <td class="px-6 py-4">
              <div class="flex justify-end gap-2">
                <button
                  class="rounded-lg p-2 text-gray-500 transition-colors hover:bg-gray-100 hover:text-indigo-600"
                  title="Edit"
                  @click="openEdit(category)"
                >
                  <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke-width="1.8" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M16.862 4.487l1.687-1.688a1.875 1.875 0 112.652 2.652L6.832 19.82a4.5 4.5 0 01-1.897 1.13l-2.685.8.8-2.685a4.5 4.5 0 011.13-1.897L16.862 4.487z" />
                  </svg>
                </button>
                <button
                  class="rounded-lg p-2 text-gray-500 transition-colors hover:bg-red-50 hover:text-red-600"
                  title="Delete"
                  @click="askDelete(category)"
                >
                  <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke-width="1.8" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M14.74 9l-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 01-2.244 2.077H8.084a2.25 2.25 0 01-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 00-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 013.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 00-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 00-7.5 0" />
                  </svg>
                </button>
              </div>
            </td>
          </tr>
          <tr v-if="sorted.length === 0">
            <td colspan="3" class="px-6 py-10 text-center text-sm text-gray-500">
              No categories yet. Add your first one.
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <Modal :open="formOpen" :title="editingId === null ? 'Add category' : 'Edit category'" @close="formOpen = false">
      <form @submit.prevent="submitForm">
        <div v-if="formError" class="mb-4 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
          {{ formError }}
        </div>
        <div>
          <label class="mb-1.5 block text-sm font-medium text-gray-700" for="category-name">Name</label>
          <input
            id="category-name"
            v-model.trim="form.name"
            type="text"
            required
            placeholder="e.g. Fiction"
            class="w-full rounded-lg border border-gray-300 px-3.5 py-2.5 text-sm shadow-sm outline-none transition-colors focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200"
          />
        </div>
        <div class="mt-6 flex justify-end gap-3">
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
      message="Are you sure you want to delete this category?"
      @close="confirmOpen = false"
      @confirm="confirmDelete"
    >
      <p class="mt-1 text-sm font-medium text-gray-900">{{ deletingName }}</p>
    </ConfirmDialog>
  </div>
</template>
