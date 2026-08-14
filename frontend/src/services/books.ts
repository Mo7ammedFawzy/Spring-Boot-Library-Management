import { reactive } from 'vue'
import { books as seed, type Book } from '../data/books'

export type BookStatus = Book['status']

export interface BookInput {
  title: string
  author: string
  category: string
  status: BookStatus
  copies: number
}

const books = reactive<Book[]>(seed)

let nextId = books.reduce((max, book) => Math.max(max, book.id), 0) + 1

export async function fetchBooks(): Promise<Book[]> {
  return [...books]
}

export async function createBook(input: BookInput): Promise<Book> {
  const book: Book = {
    id: nextId++,
    ...input
  }
  books.push(book)
  return book
}

export async function updateBook(id: number, patch: Partial<BookInput>): Promise<Book | undefined> {
  const index = books.findIndex((book) => book.id === id)
  if (index === -1) {
    return undefined
  }
  books[index] = { ...books[index], ...patch }
  return books[index]
}

export async function deleteBook(id: number): Promise<boolean> {
  const index = books.findIndex((book) => book.id === id)
  if (index === -1) {
    return false
  }
  books.splice(index, 1)
  return true
}

export function getBookCount(): number {
  return books.length
}
