import { api, isBackendUnavailable } from './api'
import * as mock from './mock'

export interface Category {
  id: number
  name: string
}

export interface CategoryInput {
  name: string
}

export async function fetchCategories(): Promise<Category[]> {
  try {
    return await api.get<Category[]>('/categories')
  } catch (error) {
    if (isBackendUnavailable(error)) return mock.fetchCategories()
    throw error
  }
}

export async function createCategory(input: CategoryInput): Promise<Category> {
  try {
    return await api.post<Category>('/categories', input)
  } catch (error) {
    if (isBackendUnavailable(error)) return mock.createCategory(input)
    throw error
  }
}

export async function updateCategory(id: number, input: CategoryInput): Promise<Category> {
  try {
    return await api.put<Category>(`/categories/${id}`, input)
  } catch (error) {
    if (isBackendUnavailable(error)) return mock.updateCategory(id, input)
    throw error
  }
}

export async function deleteCategory(id: number): Promise<void> {
  try {
    await api.delete<void>(`/categories/${id}`)
  } catch (error) {
    if (isBackendUnavailable(error)) {
      mock.deleteCategory(id)
      return
    }
    throw error
  }
}
