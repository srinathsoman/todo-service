# Todo Service

A RESTful API service for managing todo items with automatic status updates and comprehensive validation.

## Service Description

The Todo Service provides a complete backend solution for managing a simple to-do list with the following features:

- **Status Management**: Track todo status (not done, done, past due)
- **Automatic Status Updates**: Automatically marks overdue items as "past due"
- **Validation**: Comprehensive input validation and business rule enforcement
- **RESTful API**: Clean, well-documented REST endpoints
- **Docker Support**: Fully containerized for easy deployment

## API Endpoints

The Todo Service provides following REST apis
Base URL : api/v1/todo

| Method | Path             | Description                           |
|--------|------------------|---------------------------------------|
| POST   | /                | Create a new todo item                |
| PUT    | /{id}            | Update description of a todo item.    |
| PATCH  | /{id}/done       | Mark a todo as done                   |
| PATCH  | /{id}/not-done   | Mark a todo as not done               |
| GET    | ?includeAll=True | Get all todos with optional filtering |
| GET    | /{id}            | Get details of a specific todo by id  |



### Key Assumptions Made

1. **No Authentication**: As per requirements, the service does not implement user authentication
2. **In-Memory Database**: Uses H2 in-memory database for simplicity and development ease
3. **Automatic Status Updates**: Past due items are automatically updated every minute
4. **Immutable Past Due Items**: Once an item is marked as "past due", it cannot be modified
5. **Future Due Dates**: New todos must have due dates in the future
6. **Single User**: Designed for single-user scenarios

## Tech Stack

### Frameworks & Libraries
- **Java 21**
- **Spring Boot 3.5.6**
- **Maven**: Build and dependency management
- 
### Prerequisites
- Java 21 
- Maven 3.6 or higher
- Docker 

### Build Commands
```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package the application
mvn clean package

# Skip tests during packaging
mvn clean package -DskipTests
```

### Build Output
The build creates a JAR file at: `target/todoservice-0.0.1-SNAPSHOT.jar`


## How to Run the Service Locally

### Option 1: Maven (Recommended for Development)
```bash
#Run with Maven
mvn spring-boot:run
```

### Option 2: JAR File
```bash
# Build first
mvn clean package

# Run the JAR
java -jar target/todoservice-0.0.1-SNAPSHOT.jar
```

### Option 3: Docker
```bash
# Build Docker image
docker build -t todo-service .

# Run container
docker run -p 8080:8080 todo-service

```
