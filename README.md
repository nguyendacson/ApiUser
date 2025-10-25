# 🛡️ ApiUserMovie - Spring Boot Backend

A secure and scalable user management backend built with **Spring Boot**, using **MySQL**, **Docker**, **AWS**, **Redis**, **Google Clound**, **Cloundinary**, **JWT Authentication**, and modern tools like **MapStruct**, **Lombok**, and **Spring Security**.  
This project was deployed on domain https://sonnd03.io.vn/... .

---

## 📦 Features

- 🔐 Authentication & Authorization with Spring Security
- 🛡️ JWT token-based login with role-based access control
- ✅ Input validation and centralized exception handling
- 🔄 DTO ↔ Entity mapping using MapStruct
- 🔑 Password encryption with BCrypt
- 🐳 MySQL managed via Docker container
- ⚙️ Project built with Maven
- 🧰 Lombok used for cleaner code
  ...

---

## 🛠️ Technologies Used

| Technology            | Description                                                     |
|-----------------------|-----------------------------------------------------------------|
| Spring Boot           | Core framework for building the backend                         |
| MySQL (via Docker)    | Relational database running inside Docker container             |
| Maven                 | Build automation and dependency management                      |
| Lombok                | Reduces boilerplate Java code                                   |
| MapStruct             | Mapper framework for converting between DTOs and entities       |
| Spring Security       | Handles authentication and authorization                        |
| JWT                   | Provides stateless authentication using JSON Web Tokens         |
| BCrypt                | Used for secure password hashing                                |
| Bean Validation (JSR) | Input validation using annotations                              |
| Exception Handling    | Centralized handling for custom errors and validation responses |
| Redis                 | Caching when user send request to server                        |
| Google Clound         | Verifi email , create token                                     |
| Cloundinary           | Save image user                                                 |
| AWS                   | Deploy                                                          |

---


## 🔗 API Endpoints

| Method | Endpoint                    | Description                                                                |
|--------|-----------------------------|----------------------------------------------------------------------------|
| POST   | /apiUser/users              | Create a new user with default role `USER` (password will be encrypted)    |
| GET    | /apiUser/users              | Retrieve all users (admin access check required)                           |
| GET    | /apiUser/{userId}           | Get information of a specific user (only if requester is the correct user) |
| PUT    | /apiUser/{userId}           | Update information of a specific user                                      |
| DELETE | /apiUser/{userId}           | Delete a specific user                                                     |
| POST   | /apiUser/auth/login         | Login and receive JWT token                                                |
| POST   | /apiUser/auth/login-google  | Login with google                                                          |  
                                              ...                                    
___ 

## 🚀 Getting Started
### Prerequisites

- Java 24(JDK) or newer
- Maven installed
- Docker installed

### Installation & Run

```bash
# Clone the project
git clone https://github.com/yourusername/ApiUser.git
cd ApiUser

# Start MySQL container
docker-compose up -d

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run

