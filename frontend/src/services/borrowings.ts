import { api, withFallback } from './api'
import * as mock from './mock'
import type { Book } from './books'

export interface BorrowingUser {
  id: number
  name: string
  email: string
  role: string
}

export interface Borrowing {
  id: number
  user: BorrowingUser
  book: Book
  borrowDate: string
  dueDate: string
  returnDate: string | null
}

export const BORROWING_STATUSES = ['Overdue', 'Due Today', 'Borrowed', 'Returned'] as const
export type BorrowingStatus = (typeof BORROWING_STATUSES)[number]

interface BorrowResponseDto {
  id: number
  userResponse: BorrowingUser
  bookResponse: Book
  borrowDate: string
  dueDate: string
  returnDate: string | null
}

function toBorrowing(dto: BorrowResponseDto): Borrowing {
  return {
    id: dto.id,
    user: dto.userResponse,
    book: dto.bookResponse,
    borrowDate: dto.borrowDate,
    dueDate: dto.dueDate,
    returnDate: dto.returnDate
  }
}

export function todayStr(): string {
  const now = new Date()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  return `${now.getFullYear()}-${month}-${day}`
}

export function getStatus(borrowing: Pick<Borrowing, 'dueDate' | 'returnDate'>): BorrowingStatus {
  if (borrowing.returnDate) return 'Returned'
  const today = todayStr()
  if (borrowing.dueDate < today) return 'Overdue'
  if (borrowing.dueDate === today) return 'Due Today'
  return 'Borrowed'
}

export function daysUntil(dateStr: string): number {
  const due = new Date(`${dateStr}T00:00:00`).getTime()
  const today = new Date(`${todayStr()}T00:00:00`).getTime()
  return Math.round((due - today) / 86400000)
}

export function formatDate(dateStr: string | null): string {
  if (!dateStr) return '—'
  const date = new Date(`${dateStr}T00:00:00`)
  if (Number.isNaN(date.getTime())) return dateStr
  return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })
}

export const fetchBorrowings = withFallback(
  async () => {
    const list = await api.get<BorrowResponseDto[]>('/borrow')
    return list.map(toBorrowing)
  },
  () => mock.fetchBorrowings()
)

export const fetchUsers = withFallback(
  async () => api.get<BorrowingUser[]>('/users'),
  () => mock.fetchUsers()
)

export const borrowBook = withFallback(
  async (bookId: number, userId?: number) => {
    const query = userId ? `?userId=${userId}` : ''
    const dto = await api.post<BorrowResponseDto>(`/borrow/${bookId}${query}`, undefined)
    return toBorrowing(dto)
  },
  (bookId: number, userId?: number) => mock.borrowBook(bookId, userId)
)

export const returnBook = withFallback(
  async (borrowId: number) => {
    const dto = await api.post<BorrowResponseDto>(`/borrow/${borrowId}/return`, undefined)
    return toBorrowing(dto)
  },
  (borrowId: number) => mock.returnBook(borrowId)
)
