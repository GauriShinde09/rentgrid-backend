# 🚗 RentGrid – Backend

RentGrid is the backend application of an AI-powered Vehicle Rental Management System, developed using Java and Spring Boot. It provides REST APIs to manage users, vehicles, bookings, rental returns, payments-related charges, support requests and AI-powered services.

The backend follows a layered architecture and uses Spring Security with JWT authentication to secure APIs and provide role-based access for Admin and Customer users.

🌐 **Live Backend:** https://rentgrid-backend.onrender.com

🖥️ **Frontend:** https://rentgrid-app-virid.vercel.app

---

## 🚀 Features

### 1. Authentication & Authorization
- User registration and login.
- JWT-based authentication.
- Role-based authorization (ADMIN and CUSTOMER).
- Password encryption using BCrypt.
- Protected REST API endpoints.
- Forgot password and reset password functionality.

### 2. Vehicle Management
- Add new vehicles.
- Update vehicle details.
- Delete vehicles.
- Retrieve vehicle information.
- Check vehicle availability.

### 3. Booking Management
- Create vehicle bookings.
- View booking details and booking history.
- Cancel bookings.
- Request booking date modifications.
- Approve or reject date modification requests.

### 4. Rental Return Management
- Manage vehicle returns.
- Calculate late return charges.
- Calculate extra kilometre charges.
- Add vehicle damage charges.
- Generate rental invoices.

### 5. Admin Management
- Manage customers.
- Manage vehicles and bookings.
- Manage rental returns.
- View rental history.
- Generate reports.
- Manage customer support requests.

### 6. AI Integration
- AI Vehicle Finder using Groq API.
- AI-powered vehicle recommendations based on customer requirements.
- Text-based AI vehicle damage assessment.

### 7. Support Management
- Submit customer support requests.
- Retrieve support requests.
- Manage and update support request status.

---

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Java 25 | Programming Language |
| Spring Boot | Backend Framework |
| Spring Security | Authentication and Authorization |
| JWT | Token-based Authentication |
| Spring Data JPA | Database Operations |
| Hibernate | ORM |
| MySQL | Relational Database |
| Maven | Dependency Management |
| Groq API | AI Integration |
| Postman | API Testing |
| Render | Backend Deployment |
| Clever Cloud | Database Hosting |

---

## 🏗️ Backend Architecture

The application follows a layered architecture to separate business logic, API handling and database operations.

```text
RentGrid Backend
       |
       |
   Controller Layer
       |
       v
    Service Layer
       |
       v
   Repository Layer
       |
       v
    Entity Layer
       |
       v
    MySQL Database
```

### Main Layers

- **Controller:** Handles incoming HTTP requests and returns API responses.
- **Service:** Contains business logic and application operations.
- **Repository:** Performs database operations using Spring Data JPA.
- **Entity:** Represents database tables.
- **DTO:** Transfers data between frontend and backend.
- **Security:** Handles JWT authentication and role-based authorization.

---

## 🔐 Security Implementation

RentGrid uses Spring Security and JWT to secure backend APIs.

Authentication flow:

1. User sends login credentials.
2. Backend validates the credentials.
3. JWT token is generated after successful authentication.
4. Frontend sends the token with subsequent API requests.
5. JWT authentication filter validates the token.
6. Spring Security checks the user's role and permissions.

---

## ⚙️ Installation and Setup

### Prerequisites

- Java 25
- Maven
- MySQL
- Git
- Postman

### 1. Clone the Repository

```bash
git clone https://github.com/GauriShinde09/rentgrid-backend.git
```

### 2. Navigate to Project Directory

```bash
cd rentgrid-backend
```

### 3. Configure Database

Create a MySQL database:

```sql
CREATE DATABASE rentgrid_db;
```

Configure the required properties in your application configuration.

Example environment variables:

```env
DB_URL=jdbc:mysql://localhost:3306/rentgrid_db
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password

JWT_SECRET=your_jwt_secret

GROQ_API_KEY=your_groq_api_key

MAIL_USERNAME=your_email
MAIL_PASSWORD=your_email_app_password

FRONTEND_URL=http://localhost:5173
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

Backend server:

```text
http://localhost:8080
```

---

## 🌍 Deployment

The backend is deployed using:

- **Application Hosting:** Render
- **Database Hosting:** Clever Cloud MySQL

Live Backend URL:

https://rentgrid-backend.onrender.com

---

## 🧪 API Testing

Backend REST APIs were tested using **Postman**.

Testing includes:

- User registration and login.
- JWT authentication.
- Role-based access.
- Vehicle CRUD operations.
- Booking management.
- Booking modification requests.
- Rental return and additional charges.
- Support request management.
- AI API integration.

---

## 📚 Key Learning Outcomes

- Developed RESTful APIs using Spring Boot.
- Implemented JWT authentication using Spring Security.
- Applied role-based access control.
- Used Spring Data JPA and Hibernate for database operations.
- Designed entity relationships and DTOs.
- Implemented business logic for vehicle booking and rental management.
- Integrated Groq API for AI-powered functionality.
- Connected a React frontend with Spring Boot REST APIs.
- Deployed the backend and MySQL database to cloud platforms.

---

## 👩‍💻 Developer

**Gauri Shinde**

BE Graduate | Java Full Stack Developer

GitHub: https://github.com/GauriShinde09

---

⭐ If you find this project interesting, feel free to explore the repository!
