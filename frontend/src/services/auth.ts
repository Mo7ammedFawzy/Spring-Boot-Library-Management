import { api, clearToken, isAuthenticated, setToken } from './api'

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

export async function login(input: LoginInput): Promise<void> {
  const res = await api.post<AuthResponse>('/auth/login', input)
  setToken(res.token)
}

export async function register(input: RegisterInput): Promise<void> {
  const res = await api.post<AuthResponse>('/auth/register', input)
  setToken(res.token)
}

export function logout(): void {
  clearToken()
}

export function isLoggedIn(): boolean {
  return isAuthenticated()
}
