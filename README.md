# Expense Tracker API

This project is an Expense Tracker RESTful API made using Java Spring Boot, PostgreSQL, and Docker. It is designed to work in conjunction with my [Expense Tracker Frontend](https://github.com/JacobArthurs/expense-tracker-frontend), ensuring a full-stack application experience. For ease of deployment, the API is also available as a Docker image on Docker Hub. You can find the image [here](https://hub.docker.com/repository/docker/jacobarthurs/expensetracker).

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Docker installed.
- Java Development Kit (JDK) installed.
- Maven installed.
- Git installed.

## Getting Started

To get the Expense Tracker API up and running, you have two primary paths: setting it up via Docker Hub or running it locally after building it yourself. Both methods require a basic setup including the creation of environment variables and potentially modifying Docker configurations depending on your approach.

### Setup via Docker Hub

This method involves pulling a pre-built image from Docker Hub. It's ideal for those who want to quickly get the API up and running without the need for local development setup.

1. **Pull the Docker Image**

   Begin by pulling the latest Docker image of the Expense Tracker API from Docker Hub:

    ```bash
    docker pull jacobarthurs/expensetracker:latest
    ```

2. **Download `compose.yaml`**

   Download the `compose.yaml` file from the root of this repository. This file contains the necessary Docker compose configuration.

3. **Create Environment Variables**

   In the root directory of your project, create a `.env` file with the following contents to configure your environment:

   ```text
   POSTGRES_USERNAME=postgres
   POSTGRES_PASSWORD=password
   SPRING_PROFILES_ACTIVE=dev
   SECRET_KEY=%t$$upm@4XU^*eXBU88Rg&v8%8VSj7CP9&M3Snt7DLRSkaA2iTG
   ```

### Local Setup

If you're interested in a deeper understanding of the API's functionality or wish to make adjustments to the endpoints, setting up the API locally is recommended. This process involves cloning the repository, building the Java application, and managing Docker images and containers directly.

1. **Clone the Repository**

   Clone the Expense Tracker API repository and change into the project directory:

   ```bash
   git clone https://github.com/JacobArthurs/ExpenseTrackerApi.git
   cd ExpenseTrackerApi/
   ```

2. **Modify `compose.yaml`**

    Inside the `compose.yaml` file, make the following changes:
    - Replace the image `jacobarthurs/expensetracker:latest` with `expensetracker`. This step switches from using the Docker Hub image to the one you will build locally.
    - Add the following line under the PostgreSQL service's volumes section to initialize the database with dummy data upon the first creation of the volume:

        ```text
        - './docker-entrypoint-initdb.d:/docker-entrypoint-initdb.d'
        ```

3. **Create Environment Variables**

    In the root directory of your project, create a `.env` file withthe following contents to configure your environment:

    ```text
    POSTGRES_USERNAME=postgres
    POSTGRES_PASSWORD=password
    SPRING_PROFILES_ACTIVE=dev
    SECRET_KEY=%t$$upm@4XU^*eXBU88Rg&v8%8VSj7CP9&M3Snt7DLRSkaA2iTG
    ```
  
4. **Build and Start the Docker Containers**

    Use the provided `./build_and_compose.bat` script to compile and build the Java application, build the local Docker image named `expensetracker`, and then start the containers using Docker Compose:

    ```bash
    ./build_and_compose.bat
    ```

## Usage

### Exploring API Endpoints

- **Swagger UI**: To explore the available API endpoints and their documentation, visit `http://localhost:8080/api/swagger-ui` in your web browser. This interface provides a detailed overview of the API's capabilities, including request formats, response structures, and the ability to test endpoints directly.

### Authentication and User Roles (Local Setup Only)

- **Default User**: The API comes with a pre-configured default user account for initial testing and exploration. Use the following credentials:
  - **Username**: `default`
  - **Password**: `password`
  - This user account is pre-populated with dummy data to help you explore the functionality of the API without needing to create new entries.

- **Admin User**: For administrative access, including the ability to manage user roles and permissions, use the admin account:
  - **Username**: `admin`
  - **Password**: `password`
  - This admin account does not have pre-populated data but grants access to additional endpoints for administrative purposes.

### Managing Persistent Data

- **Clearing PostgreSQL Storage**: If you need to reset the database and clear all persistent data, use the following Docker command. This is useful for starting fresh or troubleshooting issues related to corrupted data.

    ```bash
    docker volume rm expensetrackerapi_postgres-data
    ```

    Please be aware that this action is irreversible and will permanently delete all data stored in the PostgreSQL database.

### Generating Documentation (Local Setup Only)

- **JavaDocs**: To generate JavaDocs for the project, which provides detailed documentation of the codebase including classes, methods, and variables, run the following Maven command in your terminal:

    ```bash
    mvn javadoc:javadoc
    ```

    Upon completion, the generated documentation will be located in `./target/site/apidocs`. This resource is invaluable for developers looking to understand or contribute to the project.

These instructions are designed to guide you through the initial setup, exploration, and management of the Expense Tracker API. Whether you are testing the API, developing new features, or managing the application's data, these steps will ensure a smooth and productive experience.

## Collaboration and Feedback

If you spot any areas for improvement or have suggestions, please don't hesitate to reach out. Whether it's through contacting me directly, opening an issue, or submitting a pull request, I welcome your input. Constructive criticism is invaluable for growth and improvement, and I appreciate your contributions to making this project better.
