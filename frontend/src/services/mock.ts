import type { Author, AuthorInput } from './authors'
import type { Category, CategoryInput } from './categories'
import type { Book, BookInput } from './books'
import type { Borrowing, BorrowingUser } from './borrowings'

let categories: Category[] = [
  { id: 1, name: 'Programming' },
  { id: 2, name: 'Software Design' },
  { id: 3, name: 'Fiction' },
  { id: 4, name: 'Science' },
  { id: 5, name: 'History' }
]

let authors: Author[] = [
  { id: 1, name: 'Robert C. Martin' },
  { id: 2, name: 'Andrew Hunt' },
  { id: 3, name: 'Erich Gamma' },
  { id: 4, name: 'Martin Fowler' },
  { id: 5, name: 'J.D. Salinger' },
  { id: 6, name: 'Aldous Huxley' },
  { id: 7, name: 'Stephen Hawking' },
  { id: 8, name: 'Richard Dawkins' },
  { id: 9, name: 'Yuval Noah Harari' },
  { id: 10, name: 'Jared Diamond' },
  { id: 11, name: 'George Orwell' },
  { id: 12, name: 'J.R.R. Tolkien' },
  { id: 13, name: 'Frank Herbert' },
  { id: 14, name: 'Andy Weir' },
  { id: 15, name: 'Carl Sagan' },
  { id: 16, name: 'Harper Lee' },
  { id: 17, name: 'F. Scott Fitzgerald' }
]

function category(id: number): Category {
  return categories.find((c) => c.id === id)!
}

function author(...ids: number[]): Author[] {
  return authors.filter((a) => ids.includes(a.id))
}

let books: Book[] = [
  { id: 1, title: 'Clean Code', description: 'A handbook of agile software craftsmanship.', availableCopies: 4, category: category(1), authors: author(1) },
  { id: 2, title: 'The Pragmatic Programmer', description: 'Tips and tricks for a pragmatic software developer.', availableCopies: 3, category: category(1), authors: author(2) },
  { id: 3, title: 'Design Patterns', description: 'Reusable object-oriented solutions to common software design problems.', availableCopies: 2, category: category(2), authors: author(3, 4) },
  { id: 4, title: 'Refactoring', description: 'Improving the design of existing code.', availableCopies: 3, category: category(2), authors: author(4) },
  { id: 5, title: 'The Catcher in the Rye', description: 'Holden Caulfield\'s journey through New York City.', availableCopies: 6, category: category(3), authors: author(5) },
  { id: 6, title: 'Brave New World', description: 'A dystopian vision of a technologically advanced future.', availableCopies: 2, category: category(3), authors: author(6) },
  { id: 7, title: 'A Brief History of Time', description: 'From the big bang to black holes.', availableCopies: 5, category: category(4), authors: author(7) },
  { id: 8, title: 'The Selfish Gene', description: 'The gene-centered view of evolution.', availableCopies: 4, category: category(4), authors: author(8) },
  { id: 9, title: 'Sapiens', description: 'A brief history of humankind.', availableCopies: 8, category: category(5), authors: author(9) },
  { id: 10, title: 'Guns, Germs, and Steel', description: 'The fates of human societies.', availableCopies: 3, category: category(5), authors: author(10) },
  { id: 11, title: '1984', description: 'A chilling portrait of a totalitarian society.', availableCopies: 7, category: category(3), authors: author(11) },
  { id: 12, title: 'The Hobbit', description: 'Bilbo Baggins\' adventure to the Lonely Mountain.', availableCopies: 5, category: category(3), authors: author(12) },
  { id: 13, title: 'Dune', description: 'Paul Atreides and the desert planet Arrakis.', availableCopies: 4, category: category(3), authors: author(13) },
  { id: 14, title: 'The Martian', description: 'An astronaut stranded on Mars fights to survive.', availableCopies: 6, category: category(4), authors: author(14) },
  { id: 15, title: 'Cosmos', description: 'The story of science across the universe.', availableCopies: 3, category: category(4), authors: author(15) },
  { id: 16, title: 'To Kill a Mockingbird', description: 'A classic of modern American literature.', availableCopies: 5, category: category(3), authors: author(16) },
  { id: 17, title: 'The Great Gatsby', description: 'The Jazz Age and the American Dream.', availableCopies: 4, category: category(3), authors: author(17) }
]

function book(id: number): Book {
  return books.find((b) => b.id === id)!
}

const users: BorrowingUser[] = [
  { id: 1, name: 'Mohammad Fawzy', email: 'admin@library.com', role: 'ADMIN' },
  { id: 2, name: 'Sara Ahmed', email: 'sara@example.com', role: 'USER' },
  { id: 3, name: 'John Doe', email: 'john@example.com', role: 'USER' },
  { id: 4, name: 'Emma Wilson', email: 'emma@example.com', role: 'USER' }
]

function dateStr(offsetDays: number): string {
  const date = new Date()
  date.setDate(date.getDate() + offsetDays)
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${date.getFullYear()}-${month}-${day}`
}

const borrowings: Borrowing[] = [
  { id: 1, user: users[1], book: book(2), borrowDate: dateStr(-20), dueDate: dateStr(-6), returnDate: null },
  { id: 2, user: users[2], book: book(5), borrowDate: dateStr(-14), dueDate: dateStr(0), returnDate: null },
  { id: 3, user: users[0], book: book(9), borrowDate: dateStr(-10), dueDate: dateStr(4), returnDate: null },
  { id: 4, user: users[3], book: book(1), borrowDate: dateStr(-30), dueDate: dateStr(-2), returnDate: dateStr(-2) }
]

function nextId(items: { id: number }[]): number {
  return Math.max(0, ...items.map((item) => item.id)) + 1
}

export function fetchBooks(): Book[] {
  return books.map((b) => ({ ...b }))
}

export function createBook(input: BookInput): Book {
  const bookCategory = categories.find((c) => c.id === input.categoryId)
  const created: Book = {
    id: nextId(books),
    title: input.title,
    description: input.description,
    availableCopies: input.availableCopies,
    category: bookCategory ?? { id: input.categoryId, name: 'Unknown' },
    authors: authors.filter((a) => input.authorIds.includes(a.id))
  }
  books.push(created)
  return { ...created }
}

export function updateBook(id: number, input: BookInput): Book {
  const index = books.findIndex((b) => b.id === id)
  if (index === -1) throw new Error(`Book with id ${id} not found`)
  const bookCategory = categories.find((c) => c.id === input.categoryId)
  const updated: Book = {
    ...books[index],
    title: input.title,
    description: input.description,
    availableCopies: input.availableCopies,
    category: bookCategory ?? books[index].category,
    authors: authors.filter((a) => input.authorIds.includes(a.id))
  }
  books[index] = updated
  return { ...updated }
}

export function deleteBook(id: number): void {
  books = books.filter((b) => b.id !== id)
}

export function fetchCategories(): Category[] {
  return [...categories]
}

export function createCategory(input: CategoryInput): Category {
  const created: Category = { id: nextId(categories), name: input.name }
  categories.push(created)
  return { ...created }
}

export function updateCategory(id: number, input: CategoryInput): Category {
  const existing = categories.find((c) => c.id === id)
  if (!existing) throw new Error(`Category with id ${id} not found`)
  existing.name = input.name
  return { ...existing }
}

export function deleteCategory(id: number): void {
  categories = categories.filter((c) => c.id !== id)
}

export function fetchAuthors(): Author[] {
  return [...authors]
}

export function createAuthor(input: AuthorInput): Author {
  const created: Author = { id: nextId(authors), name: input.name }
  authors.push(created)
  return { ...created }
}

export function updateAuthor(id: number, input: AuthorInput): Author {
  const existing = authors.find((a) => a.id === id)
  if (!existing) throw new Error(`Author with id ${id} not found`)
  existing.name = input.name
  return { ...existing }
}

export function deleteAuthor(id: number): void {
  authors = authors.filter((a) => a.id !== id)
}

export function fetchBorrowings(): Borrowing[] {
  return borrowings.map((record) => ({ ...record, user: { ...record.user }, book: { ...record.book } }))
}

export function borrowBook(bookId: number): Borrowing {
  const target = books.find((b) => b.id === bookId)
  if (!target) throw new Error(`Book with id ${bookId} not found`)
  target.availableCopies = Math.max(0, target.availableCopies - 1)
  const created: Borrowing = {
    id: nextId(borrowings),
    user: { ...users[0] },
    book: { ...target },
    borrowDate: dateStr(0),
    dueDate: dateStr(14),
    returnDate: null
  }
  borrowings.push(created)
  return created
}

export function returnBook(borrowId: number): Borrowing {
  const record = borrowings.find((b) => b.id === borrowId)
  if (!record) throw new Error(`Borrowing with id ${borrowId} not found`)
  if (record.returnDate) throw new Error('Book already returned')
  record.returnDate = dateStr(0)
  record.book.availableCopies = record.book.availableCopies + 1
  return { ...record, user: { ...record.user }, book: { ...record.book } }
}
