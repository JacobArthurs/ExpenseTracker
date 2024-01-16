# Expense Tracker API

This project is an Expense Tracker web API made using Java Spring Boot, PostgreSQL, and Docker.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Docker installed and running on your machine.
- Java Development Kit (JDK) installed.
- Maven installed.

## Getting Started
1. **Clone the repository:**

   ```
   git clone https://github.com/JacobArthurs/ExpenseTrackerApi.git
   
   cd ./ExpenseTrackerApi/
   ```
2. **Build and start the docker containers:**
    ```
    ./build_and_compose.bat
    ```

## Usage
- To explore endpoints and view documentation navigate to: `http://localhost:8080/api/swagger-ui`.

- The default user for authentication is:
  - Username: default
  - Password: password
  - This user has all dummy data populated.

- Admin user for authentication is:
  - Username: admin
  - Password: password
  - This user has no data but has access to admin-level endpoints.

- To clear docker postgres persistent storage run command:
    ```
    docker volume rm expensetrackerapi_postgres-data
    ```