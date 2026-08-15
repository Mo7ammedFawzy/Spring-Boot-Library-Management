<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import AuthLogo from '../components/auth/AuthLogo.vue'
import { register } from '../services/auth'
import { ApiError } from '../services/api'

const router = useRouter()

const fullName = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const agreeTerms = ref(false)
const showPassword = ref(false)
const showConfirmPassword = ref(false)
const isLoading = ref(false)
const error = ref('')

// Password requirement checks
const hasMinLength = computed(() => password.value.length >= 8)
const hasUppercase = computed(() => /[A-Z]/.test(password.value))
const hasNumber = computed(() => /[0-9]/.test(password.value))

async function handleSubmit() {
  isLoading.value = true
  error.value = ''

  if (password.value !== confirmPassword.value) {
    error.value = 'Passwords do not match.'
    isLoading.value = false
    return
  }

  try {
    await register({
      name: fullName.value,
      email: email.value,
      password: password.value
    })
    router.push('/dashboard')
  } catch (e) {
    error.value = e instanceof ApiError ? e.message : 'Unable to create your account. Please try again.'
  } finally {
    isLoading.value = false
  }
}
</script>

<template>
  <div class="relative min-h-svh lg:h-screen w-full bg-[#fbf9f8] flex flex-col justify-between p-3 sm:p-4 lg:px-12 lg:py-4 overflow-y-auto lg:overflow-hidden selection:bg-[#f7d7c9] selection:text-[#7a2a00]">
    <!-- Realistic Watercolor Background Illustration -->
    <div
      class="pointer-events-none absolute inset-0 select-none overflow-hidden"
      aria-hidden="true"
    >
      <img
        src="/images/auth-register-bg.jpg"
        alt="Library Reading Room"
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
    <main class="relative z-10 flex w-full flex-1 items-center justify-center lg:justify-end lg:pr-8 xl:pr-20 py-2 my-auto">
      <div class="w-full max-w-[400px] rounded-2xl bg-white p-4 sm:p-5 shadow-[0_12px_40px_-8px_rgba(0,0,0,0.08),0_4px_12px_-2px_rgba(0,0,0,0.03)] border border-[#ece7e1]">
        <!-- Title & Subtitle -->
        <div class="mb-2.5">
          <h2 class="text-xl sm:text-[21px] font-bold tracking-tight text-[#1b1c1b] leading-tight">
            Create your account
          </h2>
          <p class="mt-0.5 text-[11px] text-[#6e6863]">
            Join Athenaeum to manage your library easily
          </p>
        </div>

        <!-- Error Banner -->
        <div
          v-if="error"
          class="mb-2 flex items-center gap-2 rounded-lg border border-red-200 bg-red-50 px-3 py-2 text-[11px] text-red-700"
        >
          <UIcon
            name="i-lucide-alert-circle"
            class="size-3.5 shrink-0"
          />
          <span>{{ error }}</span>
        </div>

        <!-- Form -->
        <form
          class="space-y-2"
          @submit.prevent="handleSubmit"
        >
          <!-- Full Name Field -->
          <div>
            <label
              for="fullName"
              class="block text-[11px] font-semibold text-[#1b1c1b] mb-0.5"
            >
              Full name
            </label>
            <div class="relative">
              <div class="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-2.5 text-[#8a847e]">
                <UIcon
                  name="i-lucide-user"
                  class="size-3.5"
                />
              </div>
              <input
                id="fullName"
                v-model="fullName"
                type="text"
                required
                placeholder="Enter your full name"
                class="h-8 w-full rounded-lg border border-[#e5e1e0] bg-white pl-8 pr-2.5 text-xs text-[#1b1c1b] placeholder:text-[#a8a29e] outline-none transition-all duration-150 hover:border-[#d6d0cd] focus:border-[#9f3c11] focus:ring-2 focus:ring-[#9f3c11]/15"
              >
            </div>
          </div>

          <!-- Email Field -->
          <div>
            <label
              for="email"
              class="block text-[11px] font-semibold text-[#1b1c1b] mb-0.5"
            >
              Email address
            </label>
            <div class="relative">
              <div class="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-2.5 text-[#8a847e]">
                <UIcon
                  name="i-lucide-mail"
                  class="size-3.5"
                />
              </div>
              <input
                id="email"
                v-model="email"
                type="email"
                required
                placeholder="Enter your email"
                class="h-8 w-full rounded-lg border border-[#e5e1e0] bg-white pl-8 pr-2.5 text-xs text-[#1b1c1b] placeholder:text-[#a8a29e] outline-none transition-all duration-150 hover:border-[#d6d0cd] focus:border-[#9f3c11] focus:ring-2 focus:ring-[#9f3c11]/15"
              >
            </div>
          </div>

          <!-- Password Field -->
          <div>
            <label
              for="password"
              class="block text-[11px] font-semibold text-[#1b1c1b] mb-0.5"
            >
              Password
            </label>
            <div class="relative">
              <div class="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-2.5 text-[#8a847e]">
                <UIcon
                  name="i-lucide-lock"
                  class="size-3.5"
                />
              </div>
              <input
                id="password"
                v-model="password"
                :type="showPassword ? 'text' : 'password'"
                required
                placeholder="Create a password"
                class="h-8 w-full rounded-lg border border-[#e5e1e0] bg-white pl-8 pr-8 text-xs text-[#1b1c1b] placeholder:text-[#a8a29e] outline-none transition-all duration-150 hover:border-[#d6d0cd] focus:border-[#9f3c11] focus:ring-2 focus:ring-[#9f3c11]/15"
              >
              <button
                type="button"
                aria-label="Toggle password visibility"
                class="absolute inset-y-0 right-0 flex items-center pr-2.5 text-[#8a847e] transition-colors hover:text-[#1b1c1b]"
                @click="showPassword = !showPassword"
              >
                <UIcon
                  :name="showPassword ? 'i-lucide-eye-off' : 'i-lucide-eye'"
                  class="size-3.5"
                />
              </button>
            </div>
          </div>

          <!-- Confirm Password Field -->
          <div>
            <label
              for="confirmPassword"
              class="block text-[11px] font-semibold text-[#1b1c1b] mb-0.5"
            >
              Confirm password
            </label>
            <div class="relative">
              <div class="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-2.5 text-[#8a847e]">
                <UIcon
                  name="i-lucide-lock"
                  class="size-3.5"
                />
              </div>
              <input
                id="confirmPassword"
                v-model="confirmPassword"
                :type="showConfirmPassword ? 'text' : 'password'"
                required
                placeholder="Confirm your password"
                class="h-8 w-full rounded-lg border border-[#e5e1e0] bg-white pl-8 pr-8 text-xs text-[#1b1c1b] placeholder:text-[#a8a29e] outline-none transition-all duration-150 hover:border-[#d6d0cd] focus:border-[#9f3c11] focus:ring-2 focus:ring-[#9f3c11]/15"
              >
              <button
                type="button"
                aria-label="Toggle confirm password visibility"
                class="absolute inset-y-0 right-0 flex items-center pr-2.5 text-[#8a847e] transition-colors hover:text-[#1b1c1b]"
                @click="showConfirmPassword = !showConfirmPassword"
              >
                <UIcon
                  :name="showConfirmPassword ? 'i-lucide-eye-off' : 'i-lucide-eye'"
                  class="size-3.5"
                />
              </button>
            </div>
          </div>

          <!-- Password Requirements Box -->
          <div class="rounded-lg border border-[#d6e5e2] bg-[#f0f6f5] p-2 text-[10px] sm:text-[11px] text-[#2d4d47]">
            <div class="flex items-center gap-1.5 font-semibold text-[#1e403a] mb-1">
              <UIcon
                name="i-lucide-check-circle-2"
                class="size-3 text-[#3d8076]"
              />
              <span>Password must contain:</span>
            </div>
            <ul class="space-y-0.5 pl-4">
              <li
                class="flex items-center gap-1 transition-colors duration-150"
                :class="hasMinLength ? 'text-[#205249] font-medium' : 'text-[#5d7d77]'"
              >
                <UIcon
                  name="i-lucide-check"
                  class="size-3 shrink-0"
                  :class="hasMinLength ? 'text-[#2e7d70] stroke-[2.5]' : 'text-[#96b4ae]'"
                />
                <span>At least 8 characters</span>
              </li>
              <li
                class="flex items-center gap-1 transition-colors duration-150"
                :class="hasUppercase ? 'text-[#205249] font-medium' : 'text-[#5d7d77]'"
              >
                <UIcon
                  name="i-lucide-check"
                  class="size-3 shrink-0"
                  :class="hasUppercase ? 'text-[#2e7d70] stroke-[2.5]' : 'text-[#96b4ae]'"
                />
                <span>One uppercase letter</span>
              </li>
              <li
                class="flex items-center gap-1 transition-colors duration-150"
                :class="hasNumber ? 'text-[#205249] font-medium' : 'text-[#5d7d77]'"
              >
                <UIcon
                  name="i-lucide-check"
                  class="size-3 shrink-0"
                  :class="hasNumber ? 'text-[#2e7d70] stroke-[2.5]' : 'text-[#96b4ae]'"
                />
                <span>One number</span>
              </li>
            </ul>
          </div>

          <!-- Terms Checkbox -->
          <div class="flex items-start gap-1.5 pt-0.5">
            <input
              id="terms"
              v-model="agreeTerms"
              type="checkbox"
              required
              class="mt-0.5 h-3.5 w-3.5 rounded border-[#d6d0cd] text-[#9f3c11] accent-[#9f3c11] focus:ring-[#9f3c11]/20 cursor-pointer"
            >
            <label
              for="terms"
              class="text-[10.5px] leading-tight text-[#4b4642] cursor-pointer select-none"
            >
              I agree to the
              <a
                href="#"
                class="font-medium text-[#9f3c11] hover:underline"
                @click.prevent
              >Terms of Service</a>
              and
              <a
                href="#"
                class="font-medium text-[#9f3c11] hover:underline"
                @click.prevent
              >Privacy Policy</a>
            </label>
          </div>

          <!-- Primary Submit Button -->
          <button
            type="submit"
            :disabled="isLoading"
            class="w-full rounded-lg bg-[#9f3c11] py-2 px-3 text-xs sm:text-sm font-semibold text-white shadow-sm transition-all duration-150 hover:bg-[#8a340d] active:bg-[#7a2a00] disabled:opacity-70 disabled:cursor-not-allowed flex items-center justify-center gap-2 cursor-pointer mt-1"
          >
            <UIcon
              v-if="isLoading"
              name="i-lucide-loader-2"
              class="size-3.5 animate-spin"
            />
            <span>Create account</span>
          </button>
        </form>

        <!-- Card Footer Link -->
        <p class="mt-2.5 text-center text-[11px] text-[#6e6863]">
          Already have an account?
          <RouterLink
            to="/login"
            class="font-semibold text-[#9f3c11] transition-colors hover:text-[#7a2a00] hover:underline ml-1"
          >
            Sign in
          </RouterLink>
        </p>
      </div>
    </main>

    <!-- Page Footer -->
    <footer class="relative z-10 w-full text-center text-[10.5px] text-[#8a847e] select-none py-1 shrink-0">
      © 2024 Athenaeum Management System. All rights reserved.
    </footer>
  </div>
</template>
