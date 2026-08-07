import http from './http'
import type { ApiResponse, Book, BookPayload } from '../types'

export const bookApi = {
  getAll: () => http.get<ApiResponse<Book[]>>('/books'),
  getById: (id: number) => http.get<ApiResponse<Book>>(`/books/${id}`),
  getByCategory: (categoryId: number) =>
    http.get<ApiResponse<Book[]>>(`/books/categories/${categoryId}`),
  create: (payload: BookPayload) => http.post<ApiResponse<Book>>('/books', payload),
  update: (id: number, payload: BookPayload) =>
    http.put<ApiResponse<Book>>(`/books/${id}`, payload),
  remove: (id: number) => http.delete<ApiResponse<string>>(`/books/${id}`),
}
