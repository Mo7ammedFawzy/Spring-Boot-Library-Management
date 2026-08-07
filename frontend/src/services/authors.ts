import http from './http'
import type { ApiResponse, Author, AuthorPayload } from '../types'

export const authorApi = {
  getAll: () => http.get<ApiResponse<Author[]>>('/authors'),
  create: (payload: AuthorPayload) => http.post<ApiResponse<Author>>('/authors', payload),
}
