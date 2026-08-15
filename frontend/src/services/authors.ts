import { api } from './api'

export interface Author {
  id: number
  name: string
}

export interface AuthorInput {
  name: string
}

export async function fetchAuthors(): Promise<Author[]> {
  return api.get<Author[]>('/authors')
}

export async function createAuthor(input: AuthorInput): Promise<Author> {
  return api.post<Author>('/authors', input)
}

export async function updateAuthor(id: number, input: AuthorInput): Promise<Author> {
  return api.put<Author>(`/authors/${id}`, input)
}

export async function deleteAuthor(id: number): Promise<void> {
  await api.delete<void>(`/authors/${id}`)
}
