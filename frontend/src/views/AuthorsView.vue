<script setup lang="ts">
import { computed, onMounted,useTemplateRef, reactive, ref } from 'vue'
import Modal from '../components/Modal.vue'
import { authorApi } from '../services/authors'
import { getErrorMessage } from '../services/http'
import type { Author } from '../types'

const authors = ref<Author[]>([])
const loading = ref(true)
const error = ref('')

const modalOpen = ref(false)
const submitting = ref(false)
const authorNameRef = useTemplateRef<HTMLInputElement>("authorNameRef");
const formError = ref('')
const form = reactive({ name: '' })

const sorted = computed(() => [...authors.value].sort((a, b) => a.name.localeCompare(b.name)))

async function loadAuthors() {
  loading.value = true
  error.value = ''
  try {
    const { data } = await authorApi.getAll()
    authors.value = data.data
  } catch (err) {
    error.value = getErrorMessage(err)
  } finally {
    loading.value = false
  }
}

function openModal() {
  form.name = ''
  formError.value = ''
  modalOpen.value = true
  authorNameRef.value?.focus()
}

async function submitForm() {
  formError.value = ''
  submitting.value = true
  try {
    await authorApi.create({ name: form.name.trim() })
    modalOpen.value = false
    await loadAuthors()
  } catch (err) {
    formError.value = getErrorMessage(err)
  } finally {
    submitting.value = false
  }
}

onMounted(loadAuthors)
</script>

<template>
  <div>
    <div class="mb-6 flex items-center justify-between">
      <div>
        <h1 class="text-xl font-semibold text-gray-900">Authors</h1>
        <p class="mt-1 text-sm text-gray-500">All authors in the library.</p>
      </div>
      <button
        class="flex items-center gap-2 rounded-lg bg-indigo-600 px-4 py-2 text-sm font-medium text-white shadow-sm transition-colors hover:bg-indigo-700"
        @click="openModal"
      >
        <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
        </svg>
        Add author
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
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-200">
          <tr v-for="(author, index) in sorted" :key="author.id" class="hover:bg-gray-50">
            <td class="px-6 py-4 text-sm text-gray-500">{{ index + 1 }}</td>
            <td class="px-6 py-4 text-sm font-medium text-gray-900">{{ author.name }}</td>
          </tr>
          <tr v-if="sorted.length === 0">
            <td colspan="2" class="px-6 py-10 text-center text-sm text-gray-500">No authors yet. Add your first one.</td>
          </tr>
        </tbody>
      </table>
    </div>

    <Modal :open="modalOpen" title="Add author" @close="modalOpen = false">
      <form @submit.prevent="submitForm">
        <div v-if="formError" class="mb-4 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
          {{ formError }}
        </div>
        <div>
          <label class="mb-1.5 block text-sm font-medium text-gray-700" for="author-name">Name</label>
          <input
            id="author-name"
            ref="authorNameRef"
            v-model.trim="form.name"
            type="text"
            required
            placeholder="Author name"
            class="w-full rounded-lg border border-gray-300 px-3.5 py-2.5 text-sm shadow-sm outline-none transition-colors focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200"
          />
        </div>
        <div class="mt-6 flex justify-end gap-3">
          <button
            type="button"
            class="rounded-lg border border-gray-300 px-4 py-2 text-sm font-medium text-gray-700 transition-colors hover:bg-gray-50"
            @click="modalOpen = false"
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
  </div>
</template>
