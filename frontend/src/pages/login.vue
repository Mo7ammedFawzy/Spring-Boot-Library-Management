<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import AuthLogo from '../components/auth/AuthLogo.vue'
import { login } from '../services/auth'
import { ApiError } from '../services/api'

const router = useRouter()

const email = ref('')
const password = ref('')
const rememberMe = ref(false)
const showPassword = ref(false)
const isLoading = ref(false)
const error = ref('')

async function handleSubmit() {
  isLoading.value = true
  error.value = ''
  try {
    await login({ email: email.value, password: password.value })
    router.push('/dashboard')
  } catch (e) {
    error.value = e instanceof ApiError ? e.message : 'Unable to sign in. Please try again.'
  } finally {
    isLoading.value = false
  }
}

function handleGoogleSignIn() {}
</script>

<template>
  <div class="relative h-screen w-full bg-(--ui-bg) flex flex-col justify-between p-4 sm:p-6 lg:px-12 lg:py-6 overflow-hidden selection:bg-primary-200 selection:text-primary-800">
    <!-- Realistic Watercolor Background Illustration -->
    <div
      class="pointer-events-none absolute inset-0 select-none overflow-hidden"
      aria-hidden="true"
    >
      <img
        src="/images/auth-login-bg.jpg"
        alt="Library Interior"
        class="h-full w-full object-cover object-left lg:object-left-top"
      >
      <!-- Soft right fade overlay to seamlessly integrate card area -->
      <div class="absolute inset-y-0 right-0 w-full lg:w-1/2 bg-gradient-to-l from-(--ui-bg)/95 via-(--ui-bg)/60 to-transparent pointer-events-none" />
    </div>

    <!-- Top Header / Brand Logo -->
    <header class="relative z-10 w-full flex items-center justify-between shrink-0">
      <AuthLogo />
    </header>

    <!-- Center Content / Auth Card -->
    <main class="relative z-10 flex w-full flex-1 items-center justify-center lg:justify-end lg:pr-8 xl:pr-20 my-auto">
      <div class="w-full max-w-[420px] rounded-2xl bg-(--ui-bg-card) p-6 sm:p-8 shadow-[0_12px_40px_-8px_rgba(0,0,0,0.08),0_4px_12px_-2px_rgba(0,0,0,0.03)] border border-(--ui-border)">
        <!-- Title & Subtitle -->
        <div class="mb-5">
          <h2 class="text-[22px] sm:text-2xl font-bold tracking-tight text-default leading-tight">
            Welcome back
          </h2>
          <p class="mt-1 text-xs sm:text-sm text-dimmed">
            Sign in to your account to continue
          </p>
        </div>

        <!-- Error Banner -->
        <div
          v-if="error"
          class="mb-4 flex items-center gap-2 rounded-lg border border-red-200 bg-red-50 px-3 py-2 text-xs sm:text-sm text-red-700 dark:border-red-950 dark:bg-red-950/40 dark:text-red-300"
        >
          <UIcon
            name="i-lucide-alert-circle"
            class="size-4 shrink-0"
          />
          <span>{{ error }}</span>
        </div>

        <!-- Form -->
        <form
          class="space-y-3.5"
          @submit.prevent="handleSubmit"
        >
          <!-- Email Field -->
          <div>
            <label
              for="email"
              class="block text-xs font-semibold text-default mb-1"
            >
              Email address
            </label>
            <div class="relative">
              <div class="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3 text-dimmed">
                <UIcon
                  name="i-lucide-mail"
                  class="size-4"
                />
              </div>
              <input
                id="email"
                v-model="email"
                type="email"
                required
                placeholder="Enter your email"
                class="w-full rounded-lg border border-(--ui-border) bg-(--ui-bg-card) py-2 pl-9 pr-3 text-sm text-default placeholder:text-muted outline-none transition-all duration-150 hover:border-(--ui-border-accented) focus:border-primary focus:ring-2 focus:ring-primary/15"
              >
            </div>
          </div>

          <!-- Password Field -->
          <div>
            <label
              for="password"
              class="block text-xs font-semibold text-default mb-1"
            >
              Password
            </label>
            <div class="relative">
              <div class="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3 text-dimmed">
                <UIcon
                  name="i-lucide-lock"
                  class="size-4"
                />
              </div>
              <input
                id="password"
                v-model="password"
                :type="showPassword ? 'text' : 'password'"
                required
                placeholder="Enter your password"
                class="w-full rounded-lg border border-(--ui-border) bg-(--ui-bg-card) py-2 pl-9 pr-10 text-sm text-default placeholder:text-muted outline-none transition-all duration-150 hover:border-(--ui-border-accented) focus:border-primary focus:ring-2 focus:ring-primary/15"
              >
              <button
                type="button"
                aria-label="Toggle password visibility"
                class="absolute inset-y-0 right-0 flex items-center pr-3 text-dimmed transition-colors hover:text-default"
                @click="showPassword = !showPassword"
              >
                <UIcon
                  :name="showPassword ? 'i-lucide-eye-off' : 'i-lucide-eye'"
                  class="size-4"
                />
              </button>
            </div>
          </div>

          <!-- Forgot Password Link (Right aligned) -->
          <div class="flex justify-end pt-0.5">
            <a
              href="#"
              class="text-xs font-semibold text-primary transition-colors hover:text-primary-600 dark:hover:text-primary-300 hover:underline"
              @click.prevent
            >
              Forgot password?
            </a>
          </div>

          <!-- Remember Me Checkbox -->
          <div class="flex items-center gap-2">
            <input
              id="remember"
              v-model="rememberMe"
              type="checkbox"
              class="h-3.5 w-3.5 rounded border-(--ui-border-accented) text-primary accent-primary focus:ring-primary/20 cursor-pointer"
            >
            <label
              for="remember"
              class="text-xs font-medium text-dimmed cursor-pointer select-none"
            >
              Remember me
            </label>
          </div>

          <!-- Primary Submit Button -->
          <button
            type="submit"
            :disabled="isLoading"
            class="w-full rounded-lg bg-primary py-2.5 px-4 text-sm font-semibold text-white shadow-sm transition-all duration-150 hover:bg-primary-600 active:bg-primary-700 dark:hover:bg-primary-300 dark:active:bg-primary-200 disabled:opacity-70 disabled:cursor-not-allowed flex items-center justify-center gap-2 cursor-pointer mt-1"
          >
            <UIcon
              v-if="isLoading"
              name="i-lucide-loader-2"
              class="size-4 animate-spin"
            />
            <span>Sign in</span>
          </button>
        </form>

        <!-- Divider -->
        <div class="relative my-4 flex items-center justify-center">
          <div class="w-full border-t border-(--ui-border)" />
          <span class="absolute bg-(--ui-bg-card) px-2.5 text-[11px] font-medium text-dimmed">
            or continue with
          </span>
        </div>

        <!-- Google Sign-in Button -->
        <button
          type="button"
          :disabled="isLoading"
          class="w-full rounded-lg border border-(--ui-border) bg-(--ui-bg-card) py-2 px-4 text-xs sm:text-sm font-medium text-default transition-colors duration-150 hover:bg-(--ui-bg-accented) hover:border-(--ui-border-accented) active:bg-(--ui-bg-accented) flex items-center justify-center gap-2.5 cursor-pointer"
          @click="handleGoogleSignIn"
        >
          <svg
            class="h-4 w-4 shrink-0"
            viewBox="0 0 24 24"
          >
            <path
              fill="#4285F4"
              d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"
            />
            <path
              fill="#34A853"
              d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"
            />
            <path
              fill="#FBBC05"
              d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.06H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.94l2.85-2.22.81-.63z"
            />
            <path
              fill="#EA4335"
              d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.06l3.66 2.84c.87-2.6 3.3-4.52 6.16-4.52z"
            />
          </svg>
          <span>Sign in with Google</span>
        </button>

        <!-- Card Footer Link -->
        <p class="mt-4 text-center text-xs text-dimmed">
          Don't have an account?
          <RouterLink
            to="/register"
            class="font-semibold text-primary transition-colors hover:text-primary-600 dark:hover:text-primary-300 hover:underline ml-1"
          >
            Create account
          </RouterLink>
        </p>
      </div>
    </main>

    <!-- Page Footer -->
    <footer class="relative z-10 w-full text-center text-[11px] sm:text-xs text-dimmed select-none py-1 shrink-0">
      © 2024 Athenaeum Management System. All rights reserved.
    </footer>
  </div>
</template>
