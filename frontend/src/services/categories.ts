import { api, withFallback } from './api'
import * as mock from './mock'

export interface Category {
  id: number
  name: string
}

export interface CategoryInput {
  name: string
}

export const fetchCategories = withFallback(
  () => api.get<Category[]>('/categories'),
  () => mock.fetchCategories()
)

export const createCategory = withFallback(
  (input: CategoryInput) => api.post<Category>('/categories', input),
  (input: CategoryInput) => mock.createCategory(input)
)

export const updateCategory = withFallback(
  (id: number, input: CategoryInput) => api.put<Category>(`/categories/${id}`, input),
  (id: number, input: CategoryInput) => mock.updateCategory(id, input)
)

export const deleteCategory = withFallback(
  (id: number) => api.delete<void>(`/categories/${id}`),
  (id: number) => { mock.deleteCategory(id); return Promise.resolve() }
)
