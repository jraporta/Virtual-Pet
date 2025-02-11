## 📄Assignment

This repository contains the backend of the full-stack project **Virtual Pets**, the final assignment at the IT Academy bootcamp.
<br>
You can find the code for the frontend <a href="https://github.com/jraporta/Virtual-Pet-Frontend.git">here</a>.

## ♠️Description

This API is built using Java Spring Boot and incorporates modern development practices and patterns. Key features include:
- **Reactive programming** for efficient handling of asynchronous data streams.
- **Hexagonal Architecture**, ensuring a clean separation of concerns and flexibility in adapting to changes.
- **User authentication and authorization**:
  - Admin and User roles.
  - **JWT** (JSON Web Tokens).
- **Hybrid database setup:**
  - **PostgreSQL:** Used for user data storage.
  - **MongoDB** Used for pet data storage.

## 💻Technologies

- Frameworks & Libraries: Spring WebFlux, Swagger, JUnit, Mockito
- Databases: MongoDB, PostgreSQL
- Tools: Maven, Postman, Docker

## 📋Requirements

Ensure the following prerequisites are installed on your system:
- **Docker** (to run Docker containers)
- **Docker Compose** (to manage multi-container Docker applications)

## 🛠️Setup and Installation

Follow these steps to run the project:

1. Clone the repository.

    ```bash
    git clone https://github.com/jraporta/Virtual-Pet.git
    cd Virtual-Pet
    ```

2. Build the Docker images (ensure that Docker and Docker Compose are running):

    ```bash
    docker-compose up --build
    ```

3. Access the service at: http://localhost:8080


4. To stop the application press `Ctrl+C` or run:

    ```bash
    docker-compose down
    ```

## ▶Running without Docker

1. Install the required software:
    - JDK21, Maven, PostgreSQL and MongoDB.
2. Configure the environment variables (refer to the `.env` file)
3. Build the project: `mvn clean install`
4. Run the application: `mvn spring-boot:run`
5. Access the service at: `http://localhost:8080`
6. To stop the application press `Ctrl+C`.

## 🤝Contributions

If you want to contribute to the project:

1. Fork the repository.
2. Create a branch for your feature or bug fix.
3. Submit a pull request with a detailed description.

## 📄Documentation

API documentation is available via **Swagger**:
- **Local Environment**: <http://localhost:8080/swagger-ui.html>