import { api, isBackendUnavailable } from './api'
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

export async function fetchBooks(): Promise<Book[]> {
  try {
    return await api.get<Book[]>('/books')
  } catch (error) {
    if (isBackendUnavailable(error)) return mock.fetchBooks()
    throw error
  }
}

export async function createBook(input: BookInput): Promise<Book> {
  try {
    return await api.post<Book>('/books', input)
  } catch (error) {
    if (isBackendUnavailable(error)) return mock.createBook(input)
    throw error
  }
}

export async function updateBook(id: number, input: BookInput): Promise<Book> {
  try {
    return await api.put<Book>(`/books/${id}`, input)
  } catch (error) {
    if (isBackendUnavailable(error)) return mock.updateBook(id, input)
    throw error
  }
}

export async function deleteBook(id: number): Promise<void> {
  try {
    await api.delete<void>(`/books/${id}`)
  } catch (error) {
    if (isBackendUnavailable(error)) {
      mock.deleteBook(id)
      return
    }
    throw error
  }
}
