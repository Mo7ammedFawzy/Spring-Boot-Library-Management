# Library Management System - Spring Boot Learning Project

## Overview

A backend-focused Spring Boot project to help you learn the most important backend concepts used in real companies.

The goal is NOT only to build CRUD APIs.
The goal is to understand:

* Architecture
* Business Logic
* Security
* Database Relationships
* Clean Code
* Real-world backend development

---

# Project Idea

Build a Library Management System.

The system allows:

* Users to register and login
* Admins to manage books and categories
* Users to borrow and return books
* The system to track availability and due dates

---

# Main Features

## Authentication & Authorization

* Register
* Login
* JWT Authentication
* Roles:

    * ADMIN
    * USER

---

## Books

* Add book
* Update book
* Delete book
* Get all books
* Search books
* Pagination
* Filter by category

---

## Categories

* Add category
* Update category
* Delete category
* Get all categories

---

## Authors

* Add author
* Assign authors to books
* Multiple authors per book

---

## Borrowing System

* Borrow a book
* Return a book
* Prevent borrowing unavailable books
* Due date handling

---

# Technologies

## Backend

* Java
* Spring Boot
* Spring Web
* Spring Data JPA
* Spring Security
* JWT
* Maven

## Database

* MySQL

## Tools

* IntelliJ IDEA
* Postman
* pgAdmin

---

# Topics You Will Learn

## Spring Core

* Dependency Injection
* Beans
* Annotations
* Layered Architecture

---

## Spring Web

* REST APIs
* Controllers
* Request & Response
* HTTP Status Codes

---

## Spring Data JPA

* Entities
* Repositories
* Relationships
* Query Methods
* Pagination

---

## Relationships

### One To Many

Category -> Books

### Many To One

Book -> Category

### Many To Many

Book -> Authors

### One To Many

User -> Borrow Records

---

## Security

* Spring Security
* JWT Authentication
* Role-based Authorization
* Password Encoding

---

## Validation

Using:

* @Valid
* @NotBlank
* @Email
* @Size

Examples:

* Invalid email
* Empty title
* Weak password

---

## Exception Handling

* Custom Exceptions
* Global Exception Handler
* Standard API Error Response

Examples:

* BookNotFoundException
* UserNotFoundException
* BookUnavailableException

---

# Suggested Project Structure

```txt
src/main/java/com/example/library
│
├── config
├── controller
├── dto
├── entity
├── exception
├── mapper
├── repository
├── security
├── service
│   ├── impl
├── util
└── LibraryApplication.java
```

---

# Database Design

## User

* id
* name
* email
* password
* role

---

## Book

* id
* title
* description
* availableCopies
* category

---

## Category

* id
* name

---

## Author

* id
* name

---

## BorrowRecord

* id
* user
* book
* borrowDate
* dueDate
* returnDate

---

# PLAN MODE

# Phase 1 - Setup & Basics

Duration: 2-3 Days

## Goals

* Setup Spring Boot project
* Connect PostgreSQL
* Understand project structure

## Tasks

* [ ] Create Spring Boot project
* [ ] Add dependencies
* [ ] Configure application.properties
* [ ] Connect PostgreSQL
* [ ] Test database connection
* [ ] Create first controller
* [ ] Return simple JSON response

## Topics To Learn

* Spring Initializr
* Maven
* application.properties
* @RestController
* @GetMapping

---

# Phase 2 - Books CRUD

Duration: 3-5 Days

## Goals

* Learn Controllers
* Learn Services
* Learn Repositories
* Learn JPA basics

## Tasks

* [ ] Create Book entity
* [ ] Create BookRepository
* [ ] Create BookService
* [ ] Create BookController
* [ ] Add Create Book API
* [ ] Add Get All Books API
* [ ] Add Get Book By ID API
* [ ] Add Update Book API
* [ ] Add Delete Book API
* [ ] Test APIs in Postman

## Topics To Learn

* @Entity
* @Id
* JpaRepository
* Service Layer
* Dependency Injection

---

# Phase 3 - Categories & Relationships

Duration: 3-4 Days

## Goals

* Understand relationships
* Learn database design

## Tasks

* [ ] Create Category entity
* [ ] Add relationship between Book and Category
* [ ] Create Category CRUD
* [ ] Assign category to books
* [ ] Fetch books by category

## Topics To Learn

* @OneToMany
* @ManyToOne
* Join Columns
* Lazy vs Eager

---

# Phase 4 - Authors & ManyToMany

Duration: 2-4 Days

## Goals

* Understand ManyToMany relationships

## Tasks

* [ ] Create Author entity
* [ ] Create relationship with Book
* [ ] Add author APIs
* [ ] Assign authors to books

## Topics To Learn

* @ManyToMany
* Join Tables
* Relationship Mapping

---

# Phase 5 - Validation & Exception Handling

Duration: 2-3 Days

## Goals

* Build professional APIs

## Tasks

* [ ] Add validation annotations
* [ ] Create custom exceptions
* [ ] Create global exception handler
* [ ] Return proper error responses

## Topics To Learn

* @Valid
* @ControllerAdvice
* ResponseEntity
* Exception Handling

---

# Phase 6 - Authentication & Security

Duration: 5-7 Days

## Goals

* Learn Spring Security
* Learn JWT

## Tasks

* [ ] Create User entity
* [ ] Add registration API
* [ ] Add login API
* [ ] Encrypt passwords
* [ ] Generate JWT token
* [ ] Validate JWT token
* [ ] Protect endpoints
* [ ] Add role-based access

## Topics To Learn

* Spring Security
* JWT
* Authentication
* Authorization
* BCryptPasswordEncoder

---

# Phase 7 - Borrowing System

Duration: 4-5 Days

## Goals

* Build real business logic

## Tasks

* [ ] Create BorrowRecord entity
* [ ] Borrow book API
* [ ] Return book API
* [ ] Prevent unavailable borrowing
* [ ] Add due date logic
* [ ] Track borrowed books

## Topics To Learn

* Business Logic
* Transactions
* Data consistency

---

# Phase 8 - Advanced Features

Duration: Optional

## Tasks

* [ ] Add Pagination
* [ ] Add Sorting
* [ ] Add Search
* [ ] Add Swagger
* [ ] Add Docker
* [ ] Add Unit Testing
* [ ] Add Logging

---

# Recommended Learning Order

1. Spring Basics
2. REST APIs
3. JPA
4. Relationships
5. Validation
6. Exception Handling
7. Spring Security
8. JWT
9. Business Logic
10. Advanced Features

---

# API Examples

## Authentication

* POST /api/auth/register
* POST /api/auth/login

---

## Books

* GET /api/books
* GET /api/books/{id}
* POST /api/books
* PUT /api/books/{id}
* DELETE /api/books/{id}

---

## Categories

* GET /api/categories
* POST /api/categories

---

## Authors

* GET /api/authors
* POST /api/authors

---

## Borrowing

* POST /api/borrow/{bookId}
* POST /api/return/{borrowId}

---

# Important Notes

## Do NOT Rush

Spring Boot feels confusing at first.
That is normal.

Focus on understanding:

* How requests move
* How layers communicate
* How data is stored
* How security works

---

# Real Backend Developer Mindset

When building any feature, ask yourself:

* Should this endpoint be protected?
* Should I use DTOs?
* What validations are needed?
* What happens if data does not exist?
* What status code should I return?
* Should this operation be transactional?

---

# Final Goal

By the end of this project, you should be comfortable with:

* Building REST APIs
* Database relationships
* Authentication & Authorization
* Clean architecture
* Real backend business logic
* Writing professional backend code
