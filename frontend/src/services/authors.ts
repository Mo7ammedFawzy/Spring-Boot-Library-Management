import { api, withFallback } from './api'
import * as mock from './mock'

export interface Author {
  id: number
  name: string
}

export interface AuthorInput {
  name: string
}

export const fetchAuthors = withFallback(
  () => api.get<Author[]>('/authors'),
  () => mock.fetchAuthors()
)

export const createAuthor = withFallback(
  (input: AuthorInput) => api.post<Author>('/authors', input),
  (input: AuthorInput) => mock.createAuthor(input)
)

export const updateAuthor = withFallback(
  (id: number, input: AuthorInput) => api.put<Author>(`/authors/${id}`, input),
  (id: number, input: AuthorInput) => mock.updateAuthor(id, input)
)

export const deleteAuthor = withFallback(
  (id: number) => api.delete<void>(`/authors/${id}`),
  (id: number) => { mock.deleteAuthor(id); return Promise.resolve() }
)
