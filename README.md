# Expense Tracker

This project is an Expense Tracker web API made using Java Spring Boot, PostgreSQL, and Docker.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Docker installed on your machine.
- If building from source:
    - Java Development Kit (JDK) installed.
    - Maven installed.

## Getting Started
### Option 1: Pulling from Docker Hub

To run ExpenseTracker directly from the Docker image on Docker Hub, follow these steps:

1. ***Pull the Docker image from Docker Hub:***

    ```
    docker pull jacobarthurs/expensetracker:latest
    ```

2. ***Run the Docker container:***

    ```
    docker run -p 8080:8080 jacobarthurs/expensetracker:latest
   ```
   
### Option 2: Building from Source

To run ExpenseTracker from the source code, follow these steps:

1. **Clone the repository:**

   ```
   git clone https://github.com/jacobarthurs/expensetracker.git
   
   cd ./expensetracker
   ```
2. **Build and deploy the Docker image:**
    ```
    ./build_and_deploy.bat
   ```

## Usage
Access the application at http://localhost:8080/api.