# Domain Glossary — Athenaeum Library Management System

A two-app system: a Spring Boot backend and a Vue 3 frontend, managing a physical library's catalog and lending operations.

## Core Domain Concepts

### Book
A catalog entry with a title, description, available copy count, a single Category, and zero or more Authors. The Book is the central entity — both the Catalog and Borrowing subsystems revolve around it.

### Author
A person who wrote one or more Books. Many-to-many relationship with Book.

### Category
A genre or classification label applied to a Book. Many-to-one: each Book belongs to exactly one Category.

### BorrowRecord
A lending transaction. Tracks which User borrowed which Book, on what date, when it's due, and (optionally) when it was returned. A BorrowRecord is created when a Book is borrowed and mutated when the Book is returned.

### User
A library member with name, email, password, and a role (ADMIN or USER). Implements Spring Security's `UserDetails`. Users authenticate via JWT.

### Borrowing (frontend concept)
The frontend's view of a BorrowRecord, enriched with computed status (Overdue, Due Today, Borrowed, Returned). This is a presentation-layer concept, not a backend entity.

## Subsystems

### Catalog Subsystem
Manages Books, Authors, and Categories. Responsibilities: CRUD operations, validation that referenced Categories and Authors exist, maintaining available copy counts.

### Borrowing Subsystem
Manages lending transactions. Responsibilities: borrowing a Book (decrement copies, create BorrowRecord), returning a Book (set returnDate, increment copies), listing borrow records.

### Auth Subsystem
Manages user registration and JWT-based authentication. Responsibilities: register (create User with USER role, return JWT), login (authenticate, return JWT), JWT token generation/validation.

### Frontend Mock Subsystem
Provides in-memory fallback data when the backend is unavailable. Every API service function is wrapped with `withFallback()` to transparently fall back to mock implementations.

## Key Cross-Cutting Concerns

- **JWT Authentication**: Stateless, Bearer token in Authorization header. All endpoints except `/api/auth/**` require a valid token.
- **ApiResponse Envelope**: All successful responses wrapped in `{ success, data, message }`. Errors in `{ status, message, timestamp }`.
- **Hibernate DDL**: Schema managed by `ddl-auto=update`. No migrations.
- **Current User Resolution**: Abstracted behind `UserContext` interface with `SecurityContextUserContext` (prod) and `FakeUserContext` (test double).

## Terminology Notes

- "Borrowing" on the frontend = "BorrowRecord" on the backend. The frontend adds computed status; the backend stores raw dates.
- "Members" (frontend page, currently stubbed) = "Users" in the backend. No user management UI exists yet.
- The frontend app is branded "Athenaeum"; the backend package is `org.library`.
