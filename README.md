# 📚 Athenaeum — Library Management System

A full-stack library management system built with **Spring Boot** and **Vue 3**.

Athenaeum lets librarians register users, manage books, authors and categories, and track borrowings — all behind a clean, modern admin dashboard.

## ✨ Features

- **Authentication & Security**
  - Register / Login with JWT-based authentication
  - Password hashing (BCrypt) and role-based accounts (`ADMIN`, `USER`)
  - All API routes protected except `/api/auth/**`

- **Catalog Management**
  - Full CRUD for **Books** with search, category filtering, and availability tracking
  - CRUD for **Authors** and **Categories**
  - Books support multiple authors (many-to-many) and one category (many-to-one)

- **Borrowing System**
  - Borrow and return books with due-date handling
  - Prevents borrowing unavailable books
  - Per-user borrowing history (`BorrowRecord`)

- **Frontend**
  - App shell with collapsible sidebar, global search, notifications and a **dark / light mode** toggle
  - Data grids built on **AG Grid** with quick search, column filters, and built-in pagination
  - Dedicated pages: Dashboard, Books, Members, Borrowings, Authors, Categories, Settings, Profile, and Change Password
  - **Offline fallback** — when the backend is unreachable, the app transparently falls back to in-memory mock data so the UI stays browsable

## 🖼️ Screenshots

### Login
![Login page](frontend/public/screenshots/login.png)

### Books management
![Books page](frontend/public/screenshots/books.png)

### Dashboard
![Dashboard page](frontend/public/screenshots/dashboard.png)

## 🧱 Tech Stack

### Backend — `backend/`

| Layer | Technology |
| --- | --- |
| Language | Java 21 |
| Framework | Spring Boot 4.0.6 |
| Web | Spring WebMVC |
| Data | Spring Data JPA / Hibernate |
| Security | Spring Security + JWT (jjwt) |
| Validation | Jakarta Bean Validation |
| Database | SQL Server (`localhost:1433/library`) |
| Build | Maven, Lombok |

### Frontend — `frontend/`

| Layer | Technology |
| --- | --- |
| Framework | Vue 3 (Composition API) + Vite 7 |
| UI | Nuxt UI v4 + Tailwind CSS 4 |
| Data grid | AG Grid (community) |
| Router | vue-router (file-based) |
| Package manager | pnpm |

## 📁 Project Structure

```
.
├── backend/                    # Spring Boot API
│   └── src/main/java/org/library/
│       ├── config/
│       ├── controller/         # Auth, Book, Author, Category, Borrow
│       ├── dto/                # Request / response records
│       ├── entity/             # User, Book, Author, Category, BorrowRecord
│       ├── enums/              # Role
│       ├── exception/          # GlobalExceptionHandler + custom exceptions
│       ├── mapper/
│       ├── payload/            # ApiResponse / ApiError envelope
│       ├── repository/
│       ├── security/           # JwtAuthFilter, JwtUtil, SecurityConfig
│       ├── service/            # Interfaces + impl
│       └── utils/
└── frontend/                   # Vue 3 SPA
    └── src/
        ├── components/         # AppDataGrid, grid cell renderers, auth UI
        ├── pages/              # login, register, dashboard, books, ...
        ├── services/           # api.ts client + per-domain services
        └── lib/                # AG Grid setup
```

## 🚀 Getting Started

### Prerequisites

- **Java 21**
- **Maven** (or use the included `mvnw` wrapper)
- **SQL Server** running on `localhost:1433` (database `library` is created/updated automatically via `ddl-auto=update`)
- **Node.js** and **pnpm**

### 1. Backend

Configure the database credentials in `backend/src/main/resources/application.properties`, then run from `backend/`:

```bash
.\mvnw spring-boot:run
```

The API starts at `http://localhost:5050`.

### 2. Frontend

Run from `frontend/`:

```bash
pnpm install
pnpm dev
```

The app is served at `http://localhost:5173` and proxies `/api` requests to the backend on port `5050`.

> **Note:** All routes except `/api/auth/**` and `/error` require a valid JWT. Register an account, or log in with an existing one, to access the dashboard.

## 🔌 API Overview

All responses use a standard envelope: `{ "success": bool, "data": ..., "message": ... }`.

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login, returns a JWT token |
| GET | `/api/books` | List all books |
| GET | `/api/books/{id}` | Get a book by ID |
| GET | `/api/books/categories/{categoryId}` | Books in a category |
| POST | `/api/books` | Create a book |
| PUT | `/api/books/{id}` | Update a book |
| DELETE | `/api/books/{id}` | Delete a book |
| GET | `/api/authors` | List authors |
| POST | `/api/authors` | Create an author |
| PUT / DELETE | `/api/authors/{id}` | Update / delete an author |
| GET | `/api/categories` | List categories |
| POST | `/api/categories` | Create a category |
| PUT / DELETE | `/api/categories/{id}` | Update / delete a category |
| POST | `/api/borrow/{id}` | Borrow a book |
| GET | `/api/borrow` | List borrow records |

## 📄 License

This project is for educational purposes. See `backend/README.md` and `backend/Tasks.md` for the original learning roadmap.
