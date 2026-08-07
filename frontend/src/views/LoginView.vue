<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { getErrorMessage } from '../services/http'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()

const form = reactive({ email: '', password: '' })
const submitting = ref(false)
const error = ref('')

async function onSubmit() {
  error.value = ''
  submitting.value = true
  try {
    await auth.login(form)
    const redirect = typeof route.query.redirect === 'string' ? route.query.redirect : { name: 'books' }
    router.push(redirect)
  } catch (err) {
    error.value = getErrorMessage(err)
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <div class="flex min-h-screen">
    <div class="hidden w-1/2 flex-col justify-between bg-slate-900 p-12 text-white lg:flex">
      <div class="flex items-center gap-2">
        <svg class="h-8 w-8 text-indigo-400" fill="none" viewBox="0 0 24 24" stroke-width="1.8" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" d="M12 6.042A8.967 8.967 0 006 3.75c-1.052 0-2.062.18-3 .512v14.25A8.987 8.987 0 016 18c2.305 0 4.408.867 6 2.292m0-14.25a8.966 8.966 0 016-2.292c1.052 0 2.062.18 3 .512v14.25A8.987 8.987 0 0018 18a8.967 8.967 0 00-6 2.292m0-14.25v14.25" />
        </svg>
        <span class="text-xl font-semibold">Library</span>
      </div>
      <div>
        <h1 class="text-3xl font-semibold leading-tight">Welcome back.</h1>
        <p class="mt-3 text-slate-400">Sign in to continue to the dashboard.</p>
      </div>
      <p class="text-sm text-slate-500">Spring Boot · Vue · TypeScript · Tailwind</p>
    </div>

    <div class="flex flex-1 items-center justify-center bg-gray-50 px-6">
      <div class="w-full max-w-md">
        <h2 class="text-2xl font-semibold text-gray-900">Sign in</h2>
        <p class="mt-1 text-sm text-gray-500">Enter your credentials to access the dashboard.</p>

        <form class="mt-8 space-y-5" @submit.prevent="onSubmit">
          <div v-if="error" class="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
            {{ error }}
          </div>

          <div>
            <label class="mb-1.5 block text-sm font-medium text-gray-700" for="email">Email</label>
            <input
              id="email"
              v-model.trim="form.email"
              type="email"
              required
              placeholder="you@example.com"
              class="w-full rounded-lg border border-gray-300 px-3.5 py-2.5 text-sm shadow-sm outline-none transition-colors focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200"
            />
          </div>

          <div>
            <label class="mb-1.5 block text-sm font-medium text-gray-700" for="password">Password</label>
            <input
              id="password"
              v-model="form.password"
              type="password"
              required
              placeholder="Your password"
              class="w-full rounded-lg border border-gray-300 px-3.5 py-2.5 text-sm shadow-sm outline-none transition-colors focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200"
            />
          </div>

          <button
            type="submit"
            :disabled="submitting"
            class="w-full rounded-lg bg-indigo-600 px-4 py-2.5 text-sm font-medium text-white shadow-sm transition-colors hover:bg-indigo-700 disabled:opacity-50"
          >
            {{ submitting ? 'Signing in...' : 'Sign in' }}
          </button>
        </form>

        <p class="mt-6 text-center text-sm text-gray-500">
          No account?
          <RouterLink class="font-medium text-indigo-600 hover:text-indigo-700" :to="{ name: 'register' }">
            Create one
          </RouterLink>
        </p>
      </div>
    </div>
  </div>
</template>
