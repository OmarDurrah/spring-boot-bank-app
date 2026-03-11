# 🏦 Bank Management System (Spring Boot + React)

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

# 🏗️ Architecture

The project follows a **layered architecture**:

Controller Layer
↓
Service Layer (Business Logic)
↓
Repository Layer (Spring Data JPA)
↓
Database

---

# 🔐 Security Flow

Client (React)
↓
Login Request
↓
JWT Token Generated
↓
Client sends token in Authorization header
↓
JwtFilter validates token
↓
SecurityContext updated
↓
Request allowed to access protected endpoints

**Request header example:**

Authorization: Bearer <JWT_TOKEN>

---

# 📂 Project Structure

bankapp
│
├── controller
│ └── BankController
├── service
│ └── BankService
├── repository
│ ├── UserRepository
│ ├── BankAccountRepository
│ └── TransactionRepository
├── entity
│ ├── User
│ ├── BankAccount
│ ├── Transaction
│ └── TransactionType
├── security
│ ├── JwtFilter
│ ├── JwtService
│ ├── SecurityConfig
│ └── CorsConfig
├── exception
│ └── GlobalExceptionHandler
└── dto



---

# 📡 API Endpoints & Postman Screenshots

### 1️⃣ Register User
**POST** `/api/auth/register`  
**Body:**
```json
{
  "name": "omar",
  "password": "password123"
}

Response Example:








2️⃣ Login User

POST /api/auth/login
Body:
{
  "name": "omar",
  "password": "password123"
}

Response Example:









3️⃣ Create Bank Accounts

POST /api/accounts
Headers: Authorization: Bearer <JWT_TOKEN>
Body Example:
{
  "userId": 1,
  "initialBalance": 1000
}

Response Example:








6️⃣ Transfer

POST /api/transfer
Headers: Authorization: Bearer <JWT_TOKEN>
Body Example:

{
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 150
}

Response Example:






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
Software Engineering Student
Backend Developer (Spring Boot)