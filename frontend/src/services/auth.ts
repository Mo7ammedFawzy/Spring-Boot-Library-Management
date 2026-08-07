import http from './http'
import type { ApiResponse, AuthResponse, LoginRequest, RegisterRequest } from '../types'

export const authApi = {
  register: (payload: RegisterRequest) =>
    http.post<ApiResponse<AuthResponse>>('/auth/register', payload),

  login: (payload: LoginRequest) =>
    http.post<ApiResponse<AuthResponse>>('/auth/login', payload),
}
