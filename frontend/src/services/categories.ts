import http from './http'
import type { ApiResponse, Category, CategoryPayload } from '../types'

export const categoryApi = {
  getAll: () => http.get<ApiResponse<Category[]>>('/categories'),
  getById: (id: number) => http.get<ApiResponse<Category>>(`/categories/${id}`),
  create: (payload: CategoryPayload) => http.post<ApiResponse<Category>>('/categories', payload),
  update: (id: number, payload: CategoryPayload) =>
    http.put<ApiResponse<Category>>(`/categories/${id}`, payload),
  remove: (id: number) => http.delete<ApiResponse<string>>(`/categories/${id}`),
}
