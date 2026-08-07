export interface ApiResponse<T> {
  success: boolean
  data: T
  message: string | null
}

export interface ApiError {
  status: number
  message: string
  timestamp: string
}

export interface RegisterRequest {
  name: string
  email: string
  password: string
}

export interface LoginRequest {
  email: string
  password: string
}

export interface AuthResponse {
  token: string
}

export interface Author {
  id: number
  name: string
}

export interface AuthorPayload {
  name: string
}

export interface Category {
  id: number
  name: string
}

export interface CategoryPayload {
  name: string
}

export interface Book {
  id: number
  title: string
  description: string
  availableCopies: number
  category: Category
  authors: Author[]
}

export interface BookPayload {
  title: string
  description: string
  availableCopies: number
  categoryId: number
  authorIds: number[]
}
