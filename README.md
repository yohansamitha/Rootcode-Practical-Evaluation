# Online Library System

## Project Overview

The Online Library System is designed to allow users to sign up, borrow books, and track their borrowing history. The
system includes functionalities for user registration, book exploration, borrowing transactions, and viewing borrowing
history. It is built using **Java**, **Spring Boot**, and **Maven**.

---

## Features

1. **User Management**:
    - Users can create profiles by providing basic information.
    - Users can update or delete their profiles.
    - Users can view their borrowing history.

2. **Book Management**:
    - Users can explore the collection of books and view available copies.
    - Users can search for books by author and published year.

3. **Borrowing System**:
    - Users can borrow books if copies are available.
    - Users can return borrowed books.

4. **Security**:
    - The system uses JWT-based authentication to secure user-related APIs.
    - Public APIs for browsing books are accessible without authentication.

5. **Performance Optimization**:
    - Optimized database queries for efficient data retrieval.
    - Stateless authentication ensures scalability and fast response times.

6. **Error Handling**:
    - Global exception handling for resource not found, bad requests, and validation errors.
    - Proper error messages for invalid operations like unauthorized access, borrowing unavailable books, or returning
      unborrowed books.

---

## Assumptions

1. **Entities and Relationships**:
    - A `User` can borrow multiple books, and a `Book` can be borrowed by multiple users (many-to-many relationship
      managed via the `BorrowRecord` entity).
    - The `availableCopies` field in the `Book` entity ensures that only books with available copies can be borrowed.

2. **Borrowing Rules**:
    - A user can only borrow a book if at least one copy is available.
    - Borrowing a book decreases the `availableCopies` count by 1.
    - Returning a book increases the `availableCopies` count by 1.

3. **Security**:
    - JWT-based authentication is used for securing user-related APIs.
    - Public APIs (`/api/books/available` and `/api/books/search`) are accessible without authentication.

4. **Data Retrieval**:
    - Custom DTOs are used to fetch only the required data from the database, improving performance.
    - Borrowing history is fetched using a custom query that joins `BorrowRecord` with `Book` and `User`.

5. **Error Handling**:
    - Proper error messages are returned for invalid operations, such as:
        - Borrowing a book with no available copies.
        - Returning a book that the user did not borrow.
        - Accessing resources without proper authorization.

---

## API Endpoints

### User APIs

- **POST** `/api/users/register` - Create a new user.
- **GET** `/api/users/history` - View borrowing history (requires authentication).
- **GET** `/api/users/{id}` - Get user details by ID.
- **PUT** `/api/users` - Update user details.
- **DELETE** `/api/users/{id}` - Delete a user.

### Book APIs

- **GET** `/api/books/available` - View all available books (public).
- **GET** `/api/books/search` - Search books by author and published year (public).
- **GET** `/api/books/find/{id}` - Get book details by ID.
- **POST** `/api/books` - Add a new book.
- **PUT** `/api/books` - Update book details.
- **DELETE** `/api/books/{id}` - Delete a book.

### Borrow APIs

- **POST** `/api/borrow/{bookId}` - Borrow a book (requires authentication).
- **POST** `/api/borrow/return/{borrowId}` - Return a borrowed book (requires authentication).

---

## Technologies Used

- **Java**
- **Spring Boot**
- **Maven**
- **JWT Authentication**
- **Hibernate/JPA**

---

## Database Schema

### Entities:

1. **User**:
    - `id`, `name`, `email`, `password`, `created_at`, `updated_at`
    - Relationships: One-to-Many with `BorrowRecord`.

2. **Book**:
    - `id`, `title`, `author`, `published_year`, `available_copies`
    - Relationships: One-to-Many with `BorrowRecord`.

3. **BorrowRecord**:
    - `id`, `user_id`, `book_id`, `borrowed_at`, `returned_at`
    - Relationships: Many-to-One with `User` and `Book`.

---

## How to Run

1. Clone the repository.
2. Configure the database connection in `application.yml`.
3. Build the project using Maven:
   ```bash
   mvn clean install
4. A Postman collection is included in the project root directory (`Rootcode Practical Evaluation.postman_collection.json`) to help test all available API endpoints.