import { api } from './api'
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
  return api.get<Book[]>('/books')
}

export async function createBook(input: BookInput): Promise<Book> {
  return api.post<Book>('/books', input)
}

export async function updateBook(id: number, input: BookInput): Promise<Book> {
  return api.put<Book>(`/books/${id}`, input)
}

export async function deleteBook(id: number): Promise<void> {
  await api.delete<void>(`/books/${id}`)
}
