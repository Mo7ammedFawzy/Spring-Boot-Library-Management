# Project Context: Athenaeum Library Management System

## 1. Project Purpose and Goals

**Athenaeum** is a library management system designed to streamline the cataloging, inventory tracking, and lending operations of a physical library. 

The primary goals are:
- Maintain an accurate, searchable catalog of books, authors, and categories.
- Track physical inventory and real-time copy availability.
- Facilitate simple, reliable book borrowing and return workflows for library members.
- Monitor active loans, track due dates, and identify overdue materials.
- Provide librarians and administrators with clear visibility into circulation activity and library statistics.

---

## 2. Main Business & Domain Concepts

- **Catalog**: The complete collection of book titles registered in the library, categorized by subject and linked to authors.
- **Physical Inventory & Availability**: The physical count of copies on hand. Books have a total count and an available count that dynamically reflects active loans.
- **Lending / Loan (Borrowing)**: A time-limited agreement where a library member takes physical possession of a book copy and commits to returning it by an agreed due date.
- **Loan Lifecycle & Status**: The real-time operational state of a loan:
  - **Borrowed (Active)**: The book is currently in possession of the member and within the allowed loan period.
  - **Due Today**: The loan's due date matches the current calendar date.
  - **Overdue**: The due date has passed without the book being returned.
  - **Returned**: The book has been successfully returned to the library shelves.
- **Membership**: A registered patron profile entitled to check out books and track their personal lending history.

---

## 3. Users and Their Roles

### Administrator (Librarian / Library Manager)
- **Responsibilities**:
  - Manages the catalog: creates, updates, and deletes books, authors, and categories.
  - Monitors library circulation, all member loans, and overdue returns.
  - Manages member accounts and library-wide configuration.
  - Processes book returns and reviews operational metrics.

### Member (Patron / Borrower)
- **Responsibilities**:
  - Searches and browses the book catalog.
  - Checks out available books.
  - Views personal active loans, upcoming due dates, and past borrowing history.
  - Returns borrowed books.
  - Manages personal profile details and security credentials.

---

## 4. Important Entities and Relationships

```
+------------------+         0..* +------------------+
|      Author      | <----------> |       Book       |
+------------------+              +------------------+
                                           | 1
                                           |
                                           | 0..*
+------------------+         1    | +------------------+
|     Category     | <------------+ |   Borrow Record  |
+------------------+                +------------------+
                                             |
                                             | 0..*
                                           1 |
                                    +------------------+
                                    |       User       |
                                    +------------------+
```

### Entity Definitions

- **Book**:
  - *Attributes*: Title, Description / Summary, Available Copies count.
  - *Relationships*:
    - Belongs to exactly **one** Category.
    - Can have **zero, one, or many** Authors.
    - Can be referenced in **zero, one, or many** Borrow Records.

- **Author**:
  - *Attributes*: Name.
  - *Relationships*:
    - Associated with **zero, one, or many** Books.

- **Category**:
  - *Attributes*: Name (e.g., Fiction, Science, Philosophy, History).
  - *Relationships*:
    - Contains **zero, one, or many** Books.

- **Borrow Record (Loan)**:
  - *Attributes*: Borrow Date, Due Date, Return Date (empty while actively borrowed).
  - *Relationships*:
    - Associated with exactly **one** User (Borrower).
    - Associated with exactly **one** Book.

- **User (Member / Administrator)**:
  - *Attributes*: Full Name, Email Address, Role (`ADMIN` or `USER`).
  - *Relationships*:
    - Can have **zero, one, or many** Borrow Records.

---

## 5. Core Workflows and Business Rules

### 1. Book Borrowing Workflow
1. A member selects an available book to borrow.
2. **Availability Validation**: The system checks if `Available Copies > 0`. If `0`, borrowing is disallowed.
3. **Record Creation**: A new borrow record is generated linking the member and the book.
4. **Dates Assigned**:
   - `Borrow Date` = Current Date.
   - `Due Date` = Current Date + 14 Days (standard loan window).
   - `Return Date` = None (active loan).
5. **Inventory Decrement**: The book's available copy count is decreased by 1.

### 2. Book Return Workflow
1. A return is initiated for an active borrow record.
2. **Return Validation**: The record must not already have a recorded return date.
3. **Record Update**: `Return Date` is set to the current date.
4. **Inventory Increment**: The book's available copy count is increased by 1.

### 3. Catalog Management Workflow
- Creating or editing a book requires specifying a title, description, an existing category, one or more authors, and copy availability.
- A category must exist before a book can be assigned to it.
- Deleting an author or category does not delete the associated book, but books must retain valid taxonomic references.

### 4. Dynamic Loan Status Determination
The status of any loan is evaluated against the current date:
- If `Return Date` is present: **Returned**.
- If `Return Date` is absent and `Current Date > Due Date`: **Overdue**.
- If `Return Date` is absent and `Current Date == Due Date`: **Due Today**.
- If `Return Date` is absent and `Current Date < Due Date`: **Borrowed (Active)**.

---

## 6. Key Features and Capabilities

- **Dashboard**:
  - High-level overview of library activity: total book inventory, active borrowings, overdue loans, and quick metrics.
  - Recent lending activity feed and quick action entry points.
- **Catalog & Book Management**:
  - Browsable and searchable inventory grid.
  - Filtering by category and author.
  - Real-time display of copy availability and metadata.
  - Modal workflows for creating and editing books.
- **Circulation & Borrowing Management**:
  - Comprehensive log of all loan transactions.
  - Real-time visual status badges (Overdue, Due Today, Borrowed, Returned).
  - One-click book return actions for active loans.
- **Author & Category Management**:
  - Dedicated interfaces to manage author directory and classification taxonomy.
- **Member & Account Management**:
  - Member registration, authentication, and profile updates.
  - Password change and personal account management.

---

## 7. Business Constraints, Assumptions & Decisions

- **Title-Level Inventory Model**: The system tracks inventory as aggregate counts of available copies per book title rather than tracking individual physical item barcodes or copy serial numbers.
- **Fixed Loan Duration**: The standard lending period is fixed at **14 calendar days** from the date of loan creation.
- **Single Copy per Loan Transaction**: Each borrow record represents exactly one copy of a book loaned to one member.
- **Strict Availability Enforcement**: A member cannot borrow a book if there are no copies currently available.
- **Preserved Historical Records**: Completed (returned) loans are preserved in the system to maintain an audit trail and historical circulation records.
- **Category Exclusivity**: A book belongs to a single primary category to maintain clean, unambiguous grouping.
