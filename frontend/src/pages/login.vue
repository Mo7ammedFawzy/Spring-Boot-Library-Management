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
  <div class="relative h-screen w-full bg-[#fbf9f8] flex flex-col justify-between p-4 sm:p-6 lg:px-12 lg:py-6 overflow-hidden selection:bg-[#f7d7c9] selection:text-[#7a2a00]">
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
      <div class="absolute inset-y-0 right-0 w-full lg:w-1/2 bg-gradient-to-l from-[#fbf9f8]/95 via-[#fbf9f8]/60 to-transparent pointer-events-none" />
    </div>

    <!-- Top Header / Brand Logo -->
    <header class="relative z-10 w-full flex items-center justify-between shrink-0">
      <AuthLogo />
    </header>

    <!-- Center Content / Auth Card -->
    <main class="relative z-10 flex w-full flex-1 items-center justify-center lg:justify-end lg:pr-8 xl:pr-20 my-auto">
      <div class="w-full max-w-[420px] rounded-2xl bg-white p-6 sm:p-8 shadow-[0_12px_40px_-8px_rgba(0,0,0,0.08),0_4px_12px_-2px_rgba(0,0,0,0.03)] border border-[#ece7e1]">
        <!-- Title & Subtitle -->
        <div class="mb-5">
          <h2 class="text-[22px] sm:text-2xl font-bold tracking-tight text-[#1b1c1b] leading-tight">
            Welcome back
          </h2>
          <p class="mt-1 text-xs sm:text-sm text-[#6e6863]">
            Sign in to your account to continue
          </p>
        </div>

        <!-- Error Banner -->
        <div
          v-if="error"
          class="mb-4 flex items-center gap-2 rounded-lg border border-red-200 bg-red-50 px-3 py-2 text-xs sm:text-sm text-red-700"
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
              class="block text-xs font-semibold text-[#1b1c1b] mb-1"
            >
              Email address
            </label>
            <div class="relative">
              <div class="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3 text-[#8a847e]">
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
                class="w-full rounded-lg border border-[#e5e1e0] bg-white py-2 pl-9 pr-3 text-sm text-[#1b1c1b] placeholder:text-[#a8a29e] outline-none transition-all duration-150 hover:border-[#d6d0cd] focus:border-[#9f3c11] focus:ring-2 focus:ring-[#9f3c11]/15"
              >
            </div>
          </div>

          <!-- Password Field -->
          <div>
            <label
              for="password"
              class="block text-xs font-semibold text-[#1b1c1b] mb-1"
            >
              Password
            </label>
            <div class="relative">
              <div class="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3 text-[#8a847e]">
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
                class="w-full rounded-lg border border-[#e5e1e0] bg-white py-2 pl-9 pr-10 text-sm text-[#1b1c1b] placeholder:text-[#a8a29e] outline-none transition-all duration-150 hover:border-[#d6d0cd] focus:border-[#9f3c11] focus:ring-2 focus:ring-[#9f3c11]/15"
              >
              <button
                type="button"
                aria-label="Toggle password visibility"
                class="absolute inset-y-0 right-0 flex items-center pr-3 text-[#8a847e] transition-colors hover:text-[#1b1c1b]"
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
              class="text-xs font-semibold text-[#9f3c11] transition-colors hover:text-[#7a2a00] hover:underline"
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
              class="h-3.5 w-3.5 rounded border-[#d6d0cd] text-[#9f3c11] accent-[#9f3c11] focus:ring-[#9f3c11]/20 cursor-pointer"
            >
            <label
              for="remember"
              class="text-xs font-medium text-[#4b4642] cursor-pointer select-none"
            >
              Remember me
            </label>
          </div>

          <!-- Primary Submit Button -->
          <button
            type="submit"
            :disabled="isLoading"
            class="w-full rounded-lg bg-[#9f3c11] py-2.5 px-4 text-sm font-semibold text-white shadow-sm transition-all duration-150 hover:bg-[#8a340d] active:bg-[#7a2a00] disabled:opacity-70 disabled:cursor-not-allowed flex items-center justify-center gap-2 cursor-pointer mt-1"
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
          <div class="w-full border-t border-[#eae5e0]" />
          <span class="absolute bg-white px-2.5 text-[11px] font-medium text-[#8a847e]">
            or continue with
          </span>
        </div>

        <!-- Google Sign-in Button -->
        <button
          type="button"
          :disabled="isLoading"
          class="w-full rounded-lg border border-[#e5e1e0] bg-white py-2 px-4 text-xs sm:text-sm font-medium text-[#1b1c1b] transition-colors duration-150 hover:bg-[#faf7f5] hover:border-[#d6d0cd] active:bg-[#f2ece6] flex items-center justify-center gap-2.5 cursor-pointer"
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
        <p class="mt-4 text-center text-xs text-[#6e6863]">
          Don't have an account?
          <RouterLink
            to="/register"
            class="font-semibold text-[#9f3c11] transition-colors hover:text-[#7a2a00] hover:underline ml-1"
          >
            Create account
          </RouterLink>
        </p>
      </div>
    </main>

    <!-- Page Footer -->
    <footer class="relative z-10 w-full text-center text-[11px] sm:text-xs text-[#8a847e] select-none py-1 shrink-0">
      © 2024 Athenaeum Management System. All rights reserved.
    </footer>
  </div>
</template>
