# 🏦 Bank Management System (Spring Boot)

## 📌 Overview

This project is a **Bank Management REST API** built with **Spring Boot**, simulating core banking operations such as:

- User registration and authentication
- Bank account management
- Deposit, withdraw, and transfer
- Transaction history with pagination

The system implements **JWT-based authentication**, **transaction management**, and **optimistic locking** to ensure secure and consistent financial operations.

This project demonstrates **real-world backend architecture** using Spring Boot best practices.

---

# 🚀 Features

### Authentication
- User registration
- Secure login with **JWT tokens**
- Password hashing with **BCrypt**

### Account Management
- Create bank accounts
- Link accounts to users
- Maintain account balances

### Banking Operations
- Deposit money
- Withdraw money
- Transfer money between accounts

### Transaction History
- Record all deposits, withdrawals, and transfers
- Paginated transaction retrieval

### Security
- JWT Authentication
- Custom JWT Filter
- Spring Security configuration
- Protected API endpoints

### Concurrency Protection
- **Optimistic Locking** using `@Version`
- Prevents lost updates during simultaneous transactions

### Error Handling
- Global exception handler
- Structured API error responses

---

# Architecture

The project follows a layered architecture:

- **Controller Layer** – Handles REST API requests and responses  
- **Service Layer** – Contains business logic and transaction management  
- **Repository Layer** – Handles database operations using Spring Data JPA  
- **Database Layer** – MySQL or PostgreSQL  


---



## 🔐 Security Flow

1. **Client sends a request** to a protected API endpoint.

2. The request includes an authorization header:

  
3. The request passes through **JwtFilter (OncePerRequestFilter)**.

4. The filter **extracts the JWT token** from the request header.

5. The system **validates the token**:
- Verifies the signature
- Checks token expiration

6. If the token is valid, the system **extracts the username** from the JWT.

7. The application **loads UserDetails from the database**.

8. Spring Security **creates an Authentication object** (`UsernamePasswordAuthenticationToken`).

9. The authentication is **stored in `SecurityContextHolder`**.

10. The request continues to the **Spring DispatcherServlet**.

11. The request is routed to the **protected Controller endpoint**.

12. The controller processes the request with the **authenticated user context**.
---


## 📂 Project Structure

- **bankapp**
  - **controller**
    - BankController
  - **service**
    - BankService
  - **repository**
    - UserRepository
    - BankAccountRepository
    - TransactionRepository
  - **entity**
    - User
    - BankAccount
    - Transaction
    - TransactionType
  - **security**
    - JwtFilter
    - JwtService
    - SecurityConfig
    - CorsConfig
  - **exception**
    - GlobalExceptionHandler
  - **dto**

---

# 📡 API Endpoints & Postman Screenshots

### 1️⃣ Register User
**POST** `/api/auth/register`  
**Body:**

{
  "name": "azeez",
  "password": "omardurrah"
}

Response:

{
    "name": "azeez",
    "accounts": null,
    "id": 7
}



2️⃣ Login User

POST /api/auth/login
Body:
{
  "name": "omar",
  "password": "password123"
}

Response Example:

{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhemVleiIsImlhdCI6MTc3MzE5NDM1MSwiZXhwIjoxNzczMjgwNzUxfQ.zWBSUiSdprPqNTNUEdfePxg9G73Dmr9V3GrWSTxOqJI"
}


3️⃣ Create Bank Accounts

POST /api/accounts
Headers: Authorization: Bearer <JWT_TOKEN>
Body Example:
{
  "userId": 1,
  "initialBalance": 1000
}

Response:


{
    "balance": 1000.0,
    "id": 8
}


6️⃣ Transfer

POST /api/transfer
Headers: Authorization: Bearer <JWT_TOKEN>
Body Example:

{
    "fromAccountId": 8,
    "toAccountId": 2,
    "amount": 150.0
}


Response:

{
    "fromAccountId": 8,
    "toAccountId": 2,
    "amount": 150.0
}





⚙️ Running the Project
1️⃣ Clone the repository
git clone https://github.com/your-username/bankapp.git
2️⃣ Configure database

Update application.properties:

spring.application.name=bankapp
spring.datasource.url=jdbc:mysql://localhost:3306/bank_db
spring.datasource.username=root
spring.datasource.password=OMARdurrah@9790

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=mysecretkeymysecretkeymysecretkey
jwt.expiration=86400000

server.port=6060


3️⃣ Run the application
mvn spring-boot:run
📚 Key Concepts Demonstrated

REST API design

JWT authentication

Spring Security

Transaction management

Pagination

Optimistic locking

Clean architecture

👨‍💻 Author

Omar Durrah
Computer Engineer 
Full-Stack Developer (Spring Boot)
📧 omardorra236@gmail.com
