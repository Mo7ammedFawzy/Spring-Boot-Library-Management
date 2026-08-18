const API_BASE = '/api'
const TOKEN_KEY = 'athenaeum_token'

export function getToken(): string | null {
  return localStorage.getItem(TOKEN_KEY)
}

export function setToken(token: string): void {
  localStorage.setItem(TOKEN_KEY, token)
}

export function clearToken(): void {
  localStorage.removeItem(TOKEN_KEY)
}

export function isAuthenticated(): boolean {
  return getToken() !== null
}

interface ApiEnvelope<T> {
  success: boolean
  data: T
  message: string | null
}

interface ErrorBody {
  message?: string
  error?: string
  status?: number
}

export class ApiError extends Error {
  status: number

  constructor(status: number, message: string) {
    super(message)
    this.name = 'ApiError'
    this.status = status
  }
}

export class NetworkError extends Error {
  constructor() {
    super('Unable to reach the server.')
    this.name = 'NetworkError'
  }
}

export function isBackendUnavailable(error: unknown): boolean {
  return error instanceof NetworkError || (error instanceof ApiError && error.status >= 500)
}

async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
    ...(options.headers as Record<string, string>)
  }
  const token = getToken()
  if (token) {
    headers.Authorization = `Bearer ${token}`
  }

  let res: Response
  try {
    res = await fetch(`${API_BASE}${path}`, { ...options, headers })
  } catch {
    throw new NetworkError()
  }

  let body: unknown = null
  const text = await res.text()
  try {
    body = text ? JSON.parse(text) : null
  } catch {
    body = null
  }

  if (!res.ok) {
    const err = body as ErrorBody | null
    const message = err?.message || err?.error || `Request failed with status ${res.status}`
    throw new ApiError(res.status, message)
  }

  return (body as ApiEnvelope<T> | null)?.data as T
}

export const api = {
  get: <T>(path: string) => request<T>(path),
  post: <T>(path: string, body: unknown) => request<T>(path, { method: 'POST', body: JSON.stringify(body) }),
  put: <T>(path: string, body: unknown) => request<T>(path, { method: 'PUT', body: JSON.stringify(body) }),
  delete: <T>(path: string) => request<T>(path, { method: 'DELETE' })
}

export function withFallback<TArgs extends unknown[], T>(
  apiFn: (...args: TArgs) => Promise<T>,
  mockFn: (...args: TArgs) => T | Promise<T>
): (...args: TArgs) => Promise<T> {
  return async (...args: TArgs) => {
    try {
      return await apiFn(...args)
    } catch (error) {
      if (isBackendUnavailable(error)) {
        return await mockFn(...args)
      }
      throw error
    }
  }
}
