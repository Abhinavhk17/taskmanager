# Task Manager Application 🚀

A comprehensive RESTful API for task and project management built with Spring Boot, MongoDB, and JWT authentication.

## ✨ Features 

### 1. User Management
- ✅ User registration with validation
- ✅ Secure JWT-based authentication
- ✅ View and update user profile
- ✅ Password encryption with BCrypt

### 2. Task Management
- ✅ Create tasks with title, description, and due date
- ✅ View all tasks assigned to user
- ✅ Mark tasks as completed
- ✅ Assign tasks to team members
- ✅ Update and delete tasks
- ✅ Filter tasks by status (OPEN, IN_PROGRESS, COMPLETED, CANCELLED)
- ✅ Filter tasks by priority (LOW, MEDIUM, HIGH, URGENT)
- ✅ Search tasks by title or description

### 3. Collaboration Features
- ✅ Add comments to tasks
- ✅ Add file attachments to tasks (max 10MB)
- ✅ Track task history with timestamps

### 4. Project/Team Management
- ✅ Create projects/teams
- ✅ Invite team members to projects
- ✅ View project members
- ✅ Remove members from projects
- ✅ View tasks by project

### 5. Security
- ✅ JWT token-based authentication
- ✅ Secure logout
- ✅ Password encryption
- ✅ Role-based access control

---

## 🛠️ Technology Stack

- **Backend Framework:** Spring Boot 4.0.0
- **Language:** Java 21
- **Database:** MongoDB
- **Security:** Spring Security with JWT
- **Build Tool:** Maven
- **Additional Libraries:** Lombok, JWT (io.jsonwebtoken), Hibernate Validator, Spring Data MongoDB

---

## 📋 Prerequisites

- Java 21 or higher
- MongoDB 5.0+ (running on `localhost:27017`)
- Maven 3.6+
- Postman (for API testing)

---

## 🚀 Quick Start

### 1. Start MongoDB
```bash
# Windows (if MongoDB is installed as service)
net start MongoDB

# Or start manually
mongod
```

### 2. Configure Application
The default configuration in `src/main/resources/application.properties`:
```properties
server.port=8081
spring.data.mongodb.uri=mongodb://localhost:27017/taskmanager
jwt.secret=mySecretKeyForJWTTokenGenerationAndValidationPleaseChangeThisInProduction
jwt.expiration=86400000
```

### 3. Build and Run
```bash
cd D:\project\taskmanager
mvn clean install
mvn spring-boot:run
```

Application starts on: **http://localhost:8081**

---

## 📚 API Documentation

### Base URL
```
http://localhost:8081
```

### Authentication
All endpoints except `/api/auth/register` and `/api/auth/login` require JWT authentication.

**Header Format:**
```
Authorization: Bearer <your_jwt_token>
```

---

## 🔐 API Endpoints Summary

### Authentication (3 endpoints)

#### Register
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "email": "test@example.com",
  "password": "Test@123",
  "firstName": "Test",
  "lastName": "User",
  "phone": "1234567890"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "Test@123"
}
```
**Response:** Returns JWT token - copy and use for authenticated requests

#### Logout
```http
POST /api/auth/logout
Authorization: Bearer <token>
```

---

### User Profile (3 endpoints)

- `GET /api/users/profile` - Get current user profile
- `PUT /api/users/profile` - Update profile (firstName, lastName, email, phone)
- `GET /api/users/{userId}` - Get user by ID

**Example - Update Profile:**
```http
PUT /api/users/profile
Authorization: Bearer <token>
Content-Type: application/json

{
  "firstName": "Updated",
  "lastName": "Name",
  "email": "updated@example.com",
  "phone": "9876543210"
}
```

---

### Task Management (14 endpoints)

#### Create Task
```http
POST /api/tasks
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Complete Documentation",
  "description": "Write API documentation",
  "dueDate": "2025-12-15T10:00:00",
  "status": "OPEN",
  "priority": "HIGH",
  "assignedTo": "userId",
  "projectId": "projectId"
}
```

**Valid Status Values:** `OPEN`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`  
**Valid Priority Values:** `LOW`, `MEDIUM`, `HIGH`, `URGENT`

#### All Task Endpoints
- `POST /api/tasks` - Create task
- `GET /api/tasks` - Get all tasks
- `GET /api/tasks/{id}` - Get task by ID
- `PUT /api/tasks/{id}` - Update task
- `DELETE /api/tasks/{id}` - Delete task
- `GET /api/tasks/my-tasks` - Get tasks assigned to me
- `GET /api/tasks/created-by-me` - Get tasks I created
- `GET /api/tasks/status/{status}` - Filter by status (OPEN, IN_PROGRESS, etc.)
- `GET /api/tasks/my-tasks/status/{status}` - My tasks filtered by status
- `GET /api/tasks/search?keyword={keyword}` - Search tasks by title/description
- `GET /api/tasks/project/{projectId}` - Get tasks by project
- `PUT /api/tasks/{id}/assign/{userId}` - Assign task to user
- `PUT /api/tasks/{id}/complete` - Mark task as completed
- `POST /api/tasks/{id}/comments` - Add comment to task
- `POST /api/tasks/{id}/attachments` - Add file attachment (multipart/form-data)

#### Add Comment
```http
POST /api/tasks/{taskId}/comments
Authorization: Bearer <token>
Content-Type: application/json

{
  "content": "This is a comment"
}
```

#### Add Attachment
```http
POST /api/tasks/{taskId}/attachments
Authorization: Bearer <token>
Content-Type: multipart/form-data

file: <select your file>
```

---

### Project Management (10 endpoints)

#### Create Project
```http
POST /api/projects
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "My Project",
  "description": "Project description",
  "members": ["userId1", "userId2"]
}
```

#### All Project Endpoints
- `POST /api/projects` - Create project
- `GET /api/projects` - Get all projects
- `GET /api/projects/{id}` - Get project by ID
- `PUT /api/projects/{id}` - Update project
- `DELETE /api/projects/{id}` - Delete project
- `GET /api/projects/my-projects` - Get projects where I'm a member
- `GET /api/projects/created-by-me` - Get projects I created
- `POST /api/projects/{id}/members/{userId}` - Add member to project
- `DELETE /api/projects/{id}/members/{userId}` - Remove member from project

**Total: 30 API Endpoints**

---

## 🧪 Testing Flow Example

### Step 1: Register and Login
```bash
# 1. Register
POST http://localhost:8081/api/auth/register
{
  "username": "user1",
  "email": "user1@example.com",
  "password": "Pass@123",
  "firstName": "John",
  "lastName": "Doe"
}

# 2. Login and get token
POST http://localhost:8081/api/auth/login
{
  "username": "user1",
  "password": "Pass@123"
}
# Copy the token from response
```

### Step 2: Create Project
```bash
POST http://localhost:8081/api/projects
Authorization: Bearer <your_token>
{
  "name": "Web Development Project",
  "description": "Build task manager app"
}
# Copy the project ID
```

### Step 3: Create Tasks
```bash
POST http://localhost:8081/api/tasks
Authorization: Bearer <your_token>
{
  "title": "Setup Backend",
  "description": "Initialize Spring Boot project",
  "status": "COMPLETED",
  "priority": "HIGH",
  "dueDate": "2025-12-01T10:00:00",
  "projectId": "<project_id>"
}

POST http://localhost:8081/api/tasks
Authorization: Bearer <your_token>
{
  "title": "Design Database",
  "description": "Create MongoDB schemas",
  "status": "IN_PROGRESS",
  "priority": "MEDIUM",
  "dueDate": "2025-12-05T10:00:00",
  "projectId": "<project_id>"
}
```

### Step 4: Collaborate
```bash
# Add comment
POST http://localhost:8081/api/tasks/{taskId}/comments
Authorization: Bearer <your_token>
{
  "content": "Working on this task now"
}

# Search tasks
GET http://localhost:8081/api/tasks/search?keyword=database
Authorization: Bearer <your_token>

# Filter by status
GET http://localhost:8081/api/tasks/status/IN_PROGRESS
Authorization: Bearer <your_token>
```

---

## 📦 Import Postman Collection

A ready-to-use Postman collection is included: **`TaskManager_Postman_Collection.json`**

**To Import:**
1. Open Postman
2. Click "Import"
3. Select `TaskManager_Postman_Collection.json`
4. All 30 requests are pre-configured
5. Update the `token` variable after login

---

## 🔒 Security Features

- **JWT Authentication**: Tokens expire after 24 hours
- **Password Encryption**: BCrypt hashing
- **Protected Endpoints**: All endpoints except login/register require authentication
- **CORS Enabled**: Configure for production use
- **Input Validation**: Jakarta Bean Validation on all DTOs

---

## 📊 Database Schema

### Collections
- **users** - User accounts and profiles
- **tasks** - Tasks with embedded comments and attachments
- **projects** - Projects/teams with member lists

### Key Models
```
User {
  id, username, email, password (encrypted),
  firstName, lastName, phone, roles,
  createdAt, updatedAt, enabled
}

Task {
  id, title, description, dueDate,
  status, priority,
  createdBy, assignedTo, projectId,
  comments[], attachments[],
  createdAt, updatedAt, completedAt
}

Project {
  id, name, description,
  createdBy, members[],
  createdAt, updatedAt, active
}
```

---

## ⚙️ Configuration

Edit `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8081

# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/taskmanager
spring.data.mongodb.database=taskmanager

# JWT Configuration
jwt.secret=mySecretKeyForJWTTokenGenerationAndValidationPleaseChangeThisInProduction
jwt.expiration=86400000

# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

---

## 🐛 Troubleshooting

### Port 8080 already in use
✅ **Fixed** - Application runs on port **8081**

### MongoDB connection refused
```bash
# Check if MongoDB is running
# Windows: Check services (services.msc)
# Or start manually: mongod
```

### Unauthorized error (401)
✅ Make sure you're using the token from login response  
✅ Format: `Authorization: Bearer <token>`  
✅ Token expires after 24 hours - login again if expired

### JSON parse error - Invalid enum
✅ Use correct status values: `OPEN`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`  
✅ Use correct priority values: `LOW`, `MEDIUM`, `HIGH`, `URGENT`

### Cannot deserialize value error
✅ Check date format: Use `2025-12-15T10:00:00` (ISO 8601)

---

## 📝 Enum Reference

### Task Status
- `OPEN` - New task, not started
- `IN_PROGRESS` - Currently being worked on
- `COMPLETED` - Task finished
- `CANCELLED` - Task cancelled

### Task Priority
- `LOW` - Low priority
- `MEDIUM` - Medium priority (default)
- `HIGH` - High priority
- `URGENT` - Urgent task

---

## 📁 Project Structure

```
taskmanager/
├── src/main/java/com/api/taskmanager/
│   ├── TaskmanagerApplication.java
│   ├── config/
│   │   └── SecurityConfig.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   ├── UserController.java
│   │   ├── TaskController.java
│   │   └── ProjectController.java
│   ├── dto/
│   │   ├── RegisterRequest.java
│   │   ├── LoginRequest.java
│   │   ├── JwtResponse.java
│   │   ├── TaskRequest.java
│   │   ├── ProjectRequest.java
│   │   └── ...
│   ├── model/
│   │   ├── User.java
│   │   ├── Task.java
│   │   ├── Project.java
│   │   ├── Comment.java
│   │   └── Attachment.java
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── TaskRepository.java
│   │   └── ProjectRepository.java
│   ├── security/
│   │   ├── JwtUtils.java
│   │   ├── AuthTokenFilter.java
│   │   └── ...
│   └── service/
│       ├── UserService.java
│       ├── TaskService.java
│       └── ProjectService.java
├── src/main/resources/
│   └── application.properties
├── pom.xml
├── TaskManager_Postman_Collection.json
└── README.md
```

---

## 🎯 Implementation Checklist

- [x] User registration with validation
- [x] Secure login with JWT
- [x] View and update user profile
- [x] Create tasks with details
- [x] View tasks assigned to me
- [x] Mark tasks as completed
- [x] Assign tasks to team members
- [x] Filter tasks by status
- [x] Search tasks by title/description
- [x] Add comments to tasks
- [x] Add file attachments to tasks
- [x] Create projects/teams
- [x] Invite members to projects
- [x] Secure logout

---

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

---

**Last Updated:** November 29, 2025  
**Status:** ✅ Production Ready  
**Version:** 0.0.1-SNAPSHOT

