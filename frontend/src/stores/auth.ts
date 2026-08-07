import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { authApi } from '../services/auth'
import { tokenStorage } from '../services/http'
import type { LoginRequest, RegisterRequest } from '../types'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(tokenStorage.get())
  const isAuthenticated = computed(() => token.value !== null)

  function applyToken(newToken: string) {
    token.value = newToken
    tokenStorage.set(newToken)
  }

  async function register(payload: RegisterRequest) {
    const { data } = await authApi.register(payload)
    applyToken(data.data.token)
  }

  async function login(payload: LoginRequest) {
    const { data } = await authApi.login(payload)
    applyToken(data.data.token)
  }

  function logout() {
    token.value = null
    tokenStorage.clear()
  }

  return { token, isAuthenticated, register, login, logout }
})
