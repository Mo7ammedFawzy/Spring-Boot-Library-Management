export interface Book {
  id: number
  title: string
  author: string
  category: string
  status: 'Available' | 'Borrowed' | 'Overdue'
  copies: number
}

const statuses: Book['status'][] = ['Available', 'Borrowed', 'Overdue']

const categories = [
  'Programming',
  'Software Design',
  'Fiction',
  'Science',
  'History'
] as const

const titles = [
  'Clean Code',
  'The Pragmatic Programmer',
  'Design Patterns',
  'Refactoring',
  'The Catcher in the Rye',
  'Brave New World',
  'A Brief History of Time',
  'The Selfish Gene',
  'Sapiens',
  'Guns, Germs, and Steel',
  'Code Complete',
  'Domain-Driven Design',
  'The Mythical Man-Month',
  'Head First Design Patterns',
  'JavaScript: The Good Parts',
  'The Clean Coder',
  'Introduction to Algorithms',
  'Structure and Interpretation of Computer Programs',
  'The Art of Computer Programming',
  'The Great Gatsby',
  'To Kill a Mockingbird',
  '1984',
  'Animal Farm',
  'The Hobbit',
  'The Lord of the Rings',
  'Dune',
  'The Martian',
  'Contact',
  'The God Delusion',
  'Cosmos',
  'The Fabric of the Cosmos',
  'Astrophysics for People in a Hurry',
  'The Guns of August',
  'A People\'s History of the United States',
  'The Rise and Fall of the Third Reich',
  'Guns of the South',
  'Genghis Khan and the Making of the Modern World',
  'The Silk Roads',
  'The Library of Congress Civil War Desk Reference'
]

const authors = [
  'Robert C. Martin',
  'Andrew Hunt',
  'Erich Gamma',
  'Martin Fowler',
  'J.D. Salinger',
  'Aldous Huxley',
  'Stephen Hawking',
  'Richard Dawkins',
  'Yuval Noah Harari',
  'Jared Diamond',
  'Steve McConnell',
  'Eric Evans',
  'Frederick P. Brooks',
  'Kathy Sierra',
  'Douglas Crockford',
  'Thomas H. Cormen',
  'Harold Abelson',
  'Donald Knuth',
  'F. Scott Fitzgerald',
  'Harper Lee',
  'George Orwell',
  'J.R.R. Tolkien',
  'Frank Herbert',
  'Andy Weir',
  'Carl Sagan',
  'Brian Greene',
  'Neil deGrasse Tyson',
  'Barbara Tuchman',
  'Howard Zinn',
  'William L. Shirer',
  'Jack Weatherford',
  'Peter Frankopan',
  'James M. McPherson'
]

const initialRows: Book[] = [
  { id: 1, title: 'Clean Code', author: 'Robert C. Martin', category: 'Programming', status: 'Available', copies: 4 },
  { id: 2, title: 'The Pragmatic Programmer', author: 'Andrew Hunt', category: 'Programming', status: 'Borrowed', copies: 3 },
  { id: 3, title: 'Design Patterns', author: 'Erich Gamma', category: 'Software Design', status: 'Available', copies: 2 },
  { id: 4, title: 'Refactoring', author: 'Martin Fowler', category: 'Software Design', status: 'Borrowed', copies: 3 },
  { id: 5, title: 'The Catcher in the Rye', author: 'J.D. Salinger', category: 'Fiction', status: 'Available', copies: 6 },
  { id: 6, title: 'Brave New World', author: 'Aldous Huxley', category: 'Fiction', status: 'Overdue', copies: 2 },
  { id: 7, title: 'A Brief History of Time', author: 'Stephen Hawking', category: 'Science', status: 'Available', copies: 5 },
  { id: 8, title: 'The Selfish Gene', author: 'Richard Dawkins', category: 'Science', status: 'Borrowed', copies: 4 },
  { id: 9, title: 'Sapiens', author: 'Yuval Noah Harari', category: 'History', status: 'Available', copies: 8 },
  { id: 10, title: 'Guns, Germs, and Steel', author: 'Jared Diamond', category: 'History', status: 'Borrowed', copies: 3 }
]

function mulberry32(seed: number) {
  return () => {
    seed |= 0
    seed = (seed + 0x6d2b79f5) | 0
    let t = Math.imul(seed ^ (seed >>> 15), 1 | seed)
    t = (t + Math.imul(t ^ (t >>> 7), 61 | t)) ^ t
    return ((t ^ (t >>> 14)) >>> 0) / 4294967296
  }
}

function buildRows(): Book[] {
  const rand = mulberry32(20260814)
  const extra: Book[] = []
  for (let i = 0; i < 75; i++) {
    const id = initialRows.length + i + 1
    extra.push({
      id,
      title: titles[Math.floor(rand() * titles.length)],
      author: authors[Math.floor(rand() * authors.length)],
      category: categories[Math.floor(rand() * categories.length)],
      status: statuses[Math.floor(rand() * statuses.length)],
      copies: 1 + Math.floor(rand() * 8)
    })
  }
  return [...initialRows, ...extra]
}

export const books: Book[] = buildRows()

export const bookCategories: string[] = [...categories]

export const statusColor: Record<Book['status'], string> = {
  Available: 'success',
  Borrowed: 'secondary',
  Overdue: 'error'
}
