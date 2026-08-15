import { api, clearToken, isAuthenticated, isBackendUnavailable, setToken } from './api'

interface AuthResponse {
  token: string
}

export interface LoginInput {
  email: string
  password: string
}

export interface RegisterInput {
  name: string
  email: string
  password: string
}

const MOCK_TOKEN = 'mock-athenaeum-token'

export async function login(input: LoginInput): Promise<void> {
  try {
    const res = await api.post<AuthResponse>('/auth/login', input)
    setToken(res.token)
  } catch (error) {
    if (isBackendUnavailable(error)) {
      setToken(MOCK_TOKEN)
      return
    }
    throw error
  }
}

export async function register(input: RegisterInput): Promise<void> {
  try {
    const res = await api.post<AuthResponse>('/auth/register', input)
    setToken(res.token)
  } catch (error) {
    if (isBackendUnavailable(error)) {
      setToken(MOCK_TOKEN)
      return
    }
    throw error
  }
}

export function logout(): void {
  clearToken()
}

export function isLoggedIn(): boolean {
  return isAuthenticated()
}
