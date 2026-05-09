# TASKS.md — Library Management System

> Track your progress here. Check off each task as you complete it.
> Do NOT skip tasks. Every task teaches something important.

---

## PHASE 1 — Setup & Basics
> Goal: Get the project running and connected to MySQL

### Project Setup
- [ ] Go to [https://start.spring.io](https://start.spring.io) and generate a new project
  - Project: Maven
  - Language: Java
  - Spring Boot: latest stable
  - Group: `com.example`
  - Artifact: `library`
  - Packaging: Jar
  - Java: 17+
- [ ] Add the following dependencies in Spring Initializr:
  - Spring Web
  - Spring Data JPA
  - MySQL Driver
  - Lombok
  - Validation
- [ ] Download and extract the project
- [ ] Open the project in IntelliJ IDEA
- [ ] Verify the project structure matches the suggested layout in README

### MySQL Setup
- [ ] Install MySQL on your machine (if not installed)
- [ ] Open MySQL Workbench
- [ ] Create a new database: `CREATE DATABASE library_db;`
- [ ] Create a MySQL user or use root for development
- [ ] Open `src/main/resources/application.properties`
- [ ] Add the following configuration:
  ```properties
  spring.datasource.url=jdbc:mysql://localhost:3306/library_db
  spring.datasource.username=YOUR_USERNAME
  spring.datasource.password=YOUR_PASSWORD
  spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
  spring.jpa.hibernate.ddl-auto=update
  spring.jpa.show-sql=true
  spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
  ```
- [ ] Run the application and confirm it starts without errors
- [ ] Confirm MySQL connection is successful (check logs)

### First Controller
- [ ] Create package `controller` under `com.example.library`
- [ ] Create `HelloController.java` inside `controller`
- [ ] Add `@RestController` annotation
- [ ] Add a `@GetMapping("/hello")` method that returns `"Library API is running!"`
- [ ] Run the app and visit `http://localhost:8080/hello` in browser
- [ ] Confirm you see the response

---

## PHASE 2 — Books CRUD
> Goal: Build your first complete feature end-to-end

### Book Entity
- [ ] Create package `entity`
- [ ] Create `Book.java` inside `entity`
- [ ] Add fields: `id`, `title`, `description`, `availableCopies`
- [ ] Annotate with `@Entity`, `@Table(name = "books")`
- [ ] Add `@Id` and `@GeneratedValue(strategy = GenerationType.IDENTITY)` on `id`
- [ ] Add `@Column` annotations where needed
- [ ] Use Lombok: add `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`
- [ ] Run the app and verify the `books` table is created in MySQL Workbench

### Book DTO
- [ ] Create package `dto`
- [ ] Create `BookRequest.java` — fields the client sends to create/update a book
- [ ] Create `BookResponse.java` — fields the API returns to the client
- [ ] Understand why we separate DTOs from Entities

### Book Repository
- [ ] Create package `repository`
- [ ] Create `BookRepository.java` interface
- [ ] Extend `JpaRepository<Book, Long>`
- [ ] No methods needed yet — JpaRepository gives you CRUD for free

### Book Service
- [ ] Create package `service`
- [ ] Create `BookService.java` interface with method signatures:
  - `BookResponse createBook(BookRequest request)`
  - `List<BookResponse> getAllBooks()`
  - `BookResponse getBookById(Long id)`
  - `BookResponse updateBook(Long id, BookRequest request)`
  - `void deleteBook(Long id)`
- [ ] Create package `service/impl`
- [ ] Create `BookServiceImpl.java` that implements `BookService`
- [ ] Inject `BookRepository` using constructor injection
- [ ] Implement all methods

### Book Controller
- [ ] Create `BookController.java` inside `controller`
- [ ] Annotate with `@RestController` and `@RequestMapping("/api/books")`
- [ ] Inject `BookService` using constructor injection
- [ ] Add the following endpoints:
  - `POST /api/books` → `createBook()`
  - `GET /api/books` → `getAllBooks()`
  - `GET /api/books/{id}` → `getBookById()`
  - `PUT /api/books/{id}` → `updateBook()`
  - `DELETE /api/books/{id}` → `deleteBook()`

### Testing in Postman
- [ ] Open Postman
- [ ] Test `POST /api/books` — create a book with title, description, availableCopies
- [ ] Test `GET /api/books` — confirm the created book is returned
- [ ] Test `GET /api/books/{id}` — fetch a book by ID
- [ ] Test `PUT /api/books/{id}` — update book title or description
- [ ] Test `DELETE /api/books/{id}` — delete the book
- [ ] Verify changes in MySQL Workbench after each operation

---

## PHASE 3 — Categories & Relationships
> Goal: Learn how entities relate to each other in the database

### Category Entity
- [ ] Create `Category.java` inside `entity`
- [ ] Add fields: `id`, `name`
- [ ] Annotate properly with `@Entity`, `@Id`, etc.
- [ ] Add `@OneToMany(mappedBy = "category")` to hold a list of books

### Update Book Entity
- [ ] Add a `category` field of type `Category` to `Book.java`
- [ ] Annotate with `@ManyToOne` and `@JoinColumn(name = "category_id")`
- [ ] Run the app and verify `category_id` column appears in the `books` table

### Category Repository, Service, Controller
- [ ] Create `CategoryRepository.java` extending `JpaRepository<Category, Long>`
- [ ] Create `CategoryService.java` interface with:
  - `CategoryResponse createCategory(CategoryRequest request)`
  - `List<CategoryResponse> getAllCategories()`
  - `CategoryResponse updateCategory(Long id, CategoryRequest request)`
  - `void deleteCategory(Long id)`
- [ ] Create `CategoryServiceImpl.java`
- [ ] Create `CategoryController.java` with full CRUD endpoints under `/api/categories`

### Assign Category to Book
- [ ] Update `BookRequest.java` to include a `categoryId` field
- [ ] Update `BookServiceImpl` to fetch the category by `categoryId` and assign it to the book
- [ ] Update `BookResponse.java` to include category name

### Filter Books by Category
- [ ] Add a custom method in `BookRepository`:
  ```java
  List<Book> findByCategoryId(Long categoryId);
  ```
- [ ] Add endpoint `GET /api/books?categoryId={id}` in `BookController`
- [ ] Test creating books under different categories
- [ ] Test fetching books filtered by category

---

## PHASE 4 — Authors & ManyToMany
> Goal: Understand join tables and many-to-many relationships

### Author Entity
- [ ] Create `Author.java` inside `entity`
- [ ] Add fields: `id`, `name`
- [ ] Annotate properly

### ManyToMany with Book
- [ ] In `Author.java`, add:
  ```java
  @ManyToMany(mappedBy = "authors")
  private List<Book> books;
  ```
- [ ] In `Book.java`, add:
  ```java
  @ManyToMany
  @JoinTable(
    name = "book_authors",
    joinColumns = @JoinColumn(name = "book_id"),
    inverseJoinColumns = @JoinColumn(name = "author_id")
  )
  private List<Author> authors;
  ```
- [ ] Run the app and verify `book_authors` join table is created in MySQL

### Author Repository, Service, Controller
- [ ] Create `AuthorRepository.java`
- [ ] Create `AuthorService.java` and `AuthorServiceImpl.java`
- [ ] Create `AuthorController.java` with:
  - `POST /api/authors` — create an author
  - `GET /api/authors` — list all authors

### Assign Authors to Books
- [ ] Add a `List<Long> authorIds` field to `BookRequest.java`
- [ ] In `BookServiceImpl`, fetch authors by their IDs and assign them to the book
- [ ] Update `BookResponse.java` to include a list of author names
- [ ] Test creating a book with multiple authors in Postman

---

## PHASE 5 — Validation & Exception Handling
> Goal: Make your APIs professional and resilient

### Input Validation
- [ ] In `BookRequest.java`, add:
  - `@NotBlank` on `title`
  - `@Size(min = 10)` on `description`
  - `@Min(0)` on `availableCopies`
- [ ] In `CategoryRequest.java`, add `@NotBlank` on `name`
- [ ] Add `@Valid` annotation to request body parameters in controllers
- [ ] Test sending invalid data in Postman — observe the default error response

### Custom Exceptions
- [ ] Create package `exception`
- [ ] Create `BookNotFoundException.java` extending `RuntimeException`
  ```java
  public BookNotFoundException(Long id) {
      super("Book not found with id: " + id);
  }
  ```
- [ ] Create `CategoryNotFoundException.java`
- [ ] Create `AuthorNotFoundException.java`
- [ ] Create `BookUnavailableException.java` (for borrowing phase)
- [ ] Update service methods to throw these exceptions instead of returning null

### Global Exception Handler
- [ ] Create `GlobalExceptionHandler.java` inside `exception`
- [ ] Annotate with `@ControllerAdvice`
- [ ] Create a standard error response class `ApiError.java` with fields: `status`, `message`, `timestamp`
- [ ] Handle `BookNotFoundException` → return 404 with ApiError body
- [ ] Handle `MethodArgumentNotValidException` → return 400 with validation errors
- [ ] Handle generic `Exception` → return 500 with a safe message
- [ ] Test each error case in Postman and confirm clean JSON error responses

---

## PHASE 6 — Authentication & Security
> Goal: Secure your API with JWT

### User Entity
- [ ] Create `User.java` inside `entity`
- [ ] Add fields: `id`, `name`, `email`, `password`, `role`
- [ ] Create an `enum Role { ADMIN, USER }` inside `entity`
- [ ] Annotate `User` properly
- [ ] Run app and verify `users` table is created

### Dependencies
- [ ] Add to `pom.xml`:
  - `spring-boot-starter-security`
  - `jjwt-api`, `jjwt-impl`, `jjwt-jackson` (io.jsonwebtoken)
- [ ] Reload Maven

### User Repository
- [ ] Create `UserRepository.java`
- [ ] Add method: `Optional<User> findByEmail(String email)`

### DTOs for Auth
- [ ] Create `RegisterRequest.java` with: `name`, `email`, `password`
  - Add `@NotBlank` and `@Email` validations
- [ ] Create `LoginRequest.java` with: `email`, `password`
- [ ] Create `AuthResponse.java` with: `token`

### JWT Utility
- [ ] Create package `security`
- [ ] Create `JwtUtil.java` inside `security`
- [ ] Add method `generateToken(UserDetails userDetails)` → returns JWT string
- [ ] Add method `extractUsername(String token)` → returns email
- [ ] Add method `isTokenValid(String token, UserDetails userDetails)` → returns boolean
- [ ] Store secret key in `application.properties` as `jwt.secret`

### Security Filter
- [ ] Create `JwtAuthFilter.java` extending `OncePerRequestFilter`
- [ ] Extract token from `Authorization: Bearer <token>` header
- [ ] Validate token and set authentication in `SecurityContextHolder`

### Security Config
- [ ] Create `SecurityConfig.java` inside `config`
- [ ] Annotate with `@Configuration` and `@EnableWebSecurity`
- [ ] Define `SecurityFilterChain` bean
- [ ] Permit `/api/auth/**` without authentication
- [ ] Require authentication for all other endpoints
- [ ] Add `JwtAuthFilter` before `UsernamePasswordAuthenticationFilter`
- [ ] Define `PasswordEncoder` bean using `BCryptPasswordEncoder`
- [ ] Define `AuthenticationManager` bean

### Auth Service & Controller
- [ ] Create `AuthService.java` with:
  - `AuthResponse register(RegisterRequest request)` — saves user, returns JWT
  - `AuthResponse login(LoginRequest request)` — validates credentials, returns JWT
- [ ] Create `AuthController.java` with:
  - `POST /api/auth/register`
  - `POST /api/auth/login`

### Role-Based Access
- [ ] Annotate admin-only endpoints (create/update/delete book) with `@PreAuthorize("hasRole('ADMIN')")`
- [ ] Add `@EnableMethodSecurity` to `SecurityConfig`
- [ ] Test: register as USER, try to delete a book → expect 403
- [ ] Test: register as ADMIN, delete a book → expect 200

---

## PHASE 7 — Borrowing System
> Goal: Implement real business logic

### BorrowRecord Entity
- [ ] Create `BorrowRecord.java` inside `entity`
- [ ] Add fields: `id`, `user`, `book`, `borrowDate`, `dueDate`, `returnDate`
- [ ] Add `@ManyToOne` on `user` and `book`
- [ ] `returnDate` is nullable (null = still borrowed)
- [ ] Run app and verify `borrow_records` table is created

### Borrow Logic
- [ ] Create `BorrowService.java` with:
  - `BorrowResponse borrowBook(Long bookId, Long userId)`
  - `BorrowResponse returnBook(Long borrowId)`
- [ ] In `borrowBook()`:
  - [ ] Fetch the book — throw `BookNotFoundException` if not found
  - [ ] Check `availableCopies > 0` — throw `BookUnavailableException` if not
  - [ ] Decrement `availableCopies` and save
  - [ ] Create a `BorrowRecord` with `borrowDate = now` and `dueDate = now + 14 days`
  - [ ] Save and return the record
- [ ] In `returnBook()`:
  - [ ] Fetch the borrow record — throw exception if not found
  - [ ] Set `returnDate = now`
  - [ ] Increment `availableCopies` on the book and save
  - [ ] Save and return the updated record

### Borrow Controller
- [ ] Create `BorrowController.java`
- [ ] `POST /api/borrow/{bookId}` — borrow a book (USER role)
- [ ] `POST /api/return/{borrowId}` — return a book (USER role)

### Testing
- [ ] Register a user and get a JWT token
- [ ] Create a book with `availableCopies = 1` as ADMIN
- [ ] Borrow the book as USER
- [ ] Try to borrow the same book again → expect `BookUnavailableException`
- [ ] Return the book
- [ ] Borrow it again → should work now

---

## PHASE 8 — Advanced Features (Optional)
> Goal: Go beyond basics

### Pagination & Sorting
- [ ] Update `getAllBooks()` to accept `Pageable` parameter
- [ ] Add `@PageableDefault(size = 10, sort = "title")` to controller
- [ ] Test with `GET /api/books?page=0&size=5&sort=title,asc`

### Search
- [ ] Add to `BookRepository`:
  ```java
  List<Book> findByTitleContainingIgnoreCase(String keyword);
  ```
- [ ] Add `GET /api/books/search?keyword=java` endpoint

### Swagger / OpenAPI
- [ ] Add `springdoc-openapi-starter-webmvc-ui` dependency to `pom.xml`
- [ ] Run app and visit `http://localhost:8080/swagger-ui.html`
- [ ] Add `@Operation` and `@ApiResponse` annotations to key endpoints

### Unit Testing
- [ ] Add `spring-boot-starter-test` (already included by default)
- [ ] Write test for `BookServiceImpl.createBook()`
- [ ] Write test for borrow logic (book unavailable case)
- [ ] Run tests with `mvn test`

### Logging
- [ ] Add SLF4J logger to service classes: `private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);`
- [ ] Log key actions: book created, book borrowed, user registered
- [ ] Configure log level in `application.properties`: `logging.level.com.example.library=DEBUG`

### Docker (Optional)
- [ ] Create a `Dockerfile` at the project root
- [ ] Create `docker-compose.yml` with app + MySQL services
- [ ] Build image and run with `docker-compose up`

---

## CHECKLIST SUMMARY

| Phase | Status |
|-------|--------|
| Phase 1 — Setup & Basics | ⬜ Not Started |
| Phase 2 — Books CRUD | ⬜ Not Started |
| Phase 3 — Categories & Relationships | ⬜ Not Started |
| Phase 4 — Authors & ManyToMany | ⬜ Not Started |
| Phase 5 — Validation & Exception Handling | ⬜ Not Started |
| Phase 6 — Authentication & Security | ⬜ Not Started |
| Phase 7 — Borrowing System | ⬜ Not Started |
| Phase 8 — Advanced Features | ⬜ Optional |

---

> **Remember:** The goal is not to finish fast. The goal is to understand every line you write.