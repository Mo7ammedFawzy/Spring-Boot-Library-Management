import { api, isBackendUnavailable } from './api'
import * as mock from './mock'

export interface Author {
  id: number
  name: string
}

export interface AuthorInput {
  name: string
}

export async function fetchAuthors(): Promise<Author[]> {
  try {
    return await api.get<Author[]>('/authors')
  } catch (error) {
    if (isBackendUnavailable(error)) return mock.fetchAuthors()
    throw error
  }
}

export async function createAuthor(input: AuthorInput): Promise<Author> {
  try {
    return await api.post<Author>('/authors', input)
  } catch (error) {
    if (isBackendUnavailable(error)) return mock.createAuthor(input)
    throw error
  }
}

export async function updateAuthor(id: number, input: AuthorInput): Promise<Author> {
  try {
    return await api.put<Author>(`/authors/${id}`, input)
  } catch (error) {
    if (isBackendUnavailable(error)) return mock.updateAuthor(id, input)
    throw error
  }
}

export async function deleteAuthor(id: number): Promise<void> {
  try {
    await api.delete<void>(`/authors/${id}`)
  } catch (error) {
    if (isBackendUnavailable(error)) {
      mock.deleteAuthor(id)
      return
    }
    throw error
  }
}
