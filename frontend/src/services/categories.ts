import { api } from './api'

export interface Category {
  id: number
  name: string
}

export interface CategoryInput {
  name: string
}

export async function fetchCategories(): Promise<Category[]> {
  return api.get<Category[]>('/categories')
}

export async function createCategory(input: CategoryInput): Promise<Category> {
  return api.post<Category>('/categories', input)
}

export async function updateCategory(id: number, input: CategoryInput): Promise<Category> {
  return api.put<Category>(`/categories/${id}`, input)
}

export async function deleteCategory(id: number): Promise<void> {
  await api.delete<void>(`/categories/${id}`)
}
