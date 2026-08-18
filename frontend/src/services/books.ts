import { api, withFallback } from './api'
import * as mock from './mock'
import type { Author } from './authors'
import type { Category } from './categories'

export interface Book {
  id: number
  title: string
  description: string
  availableCopies: number
  category: Category
  authors: Author[]
}

export interface BookInput {
  title: string
  description: string
  availableCopies: number
  categoryId: number
  authorIds: number[]
}

export const fetchBooks = withFallback(
  () => api.get<Book[]>('/books'),
  () => mock.fetchBooks()
)

export const createBook = withFallback(
  (input: BookInput) => api.post<Book>('/books', input),
  (input: BookInput) => mock.createBook(input)
)

export const updateBook = withFallback(
  (id: number, input: BookInput) => api.put<Book>(`/books/${id}`, input),
  (id: number, input: BookInput) => mock.updateBook(id, input)
)

export const deleteBook = withFallback(
  (id: number) => api.delete<void>(`/books/${id}`),
  (id: number) => { mock.deleteBook(id); return Promise.resolve() }
)
