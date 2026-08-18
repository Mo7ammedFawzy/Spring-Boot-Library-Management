import { api, clearToken, isAuthenticated, withFallback, setToken } from './api'

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

export const login = withFallback(
  async (input: LoginInput) => {
    const res = await api.post<AuthResponse>('/auth/login', input)
    setToken(res.token)
  },
  async (_input: LoginInput) => {
    setToken(MOCK_TOKEN)
  }
)

export const register = withFallback(
  async (input: RegisterInput) => {
    const res = await api.post<AuthResponse>('/auth/register', input)
    setToken(res.token)
  },
  async (_input: RegisterInput) => {
    setToken(MOCK_TOKEN)
  }
)

export function logout(): void {
  clearToken()
}

export function isLoggedIn(): boolean {
  return isAuthenticated()
}
