# orders
Building a Microservice with Spring Boot, MongoDB, Docker, and TestContainers for integration testing

## Objective:
- develop a Spring Boot microservice that manages a connection between "Product" and "Order" entities
- provide OpenAPI 3.1 documentation
- utilizes transactions for data consistency
- the service should use MongoDB for data storage and be containerized with Docker
- Java 21 is the preferred programming language

## Requirements:

### Project Setup:
- Establish a new Spring Boot project with Java 21 as the programming language.
- Utilize either Maven or Gradle for project management and dependencies.
### Docker Containerization:
- Craft a Dockerfile for your Spring Boot application.
- Create a Docker Compose configuration to orchestrate containers for your Spring Boot application and MongoDB.
- Ensure seamless communication between the containers.
### Spring Boot Microservice:
- Create an API to handle both "Product" and "Order" entities.
- Define data models for "Product" and "Order" using OpenAPI 3.1 specifications.
- Implement endpoints for the basic operations (Create, Read, Update, Delete) for "Product" and "Order."
### Product-Order Relationship:
- Implement operations for creating, retrieving, updating, and deleting orders.
- Establish a connection between "Product" and "Order," allowing orders to contain multiple products.
- Include API endpoints for adding products to an order executing transactions.
### OpenAPI 3.1 Documentation:
- Formulate the API specifications utilizing OpenAPI 3.1.
- Utilize it in Spring Boot application in Controllers and DTOs.
### Integration Testing with TestContainers:
- Substitute traditional unit testing with integration testing using Spring Boot.
- Use TestContainers to configure and deconfigure MongoDB containers during integration testing.
### Non-Functional Requirement: Scalability and Concurrency Handling 
- The system should be designed to handle up to 100 parallel threads concurrently adding and removing products from the same order. It should exhibit robust performance and maintain data consistency even under this level of concurrent load. The system must ensure that operations on the same order are synchronized effectively, preventing data corruption or conflicts.
- This requirement emphasizes the system's ability to scale and efficiently manage a high volume of concurrent operations on orders while maintaining data integrity and ensuring a responsive user experience even under heavy load.

## Submission:
Package the entire source code, related files, and documentation into a .zip archive.

## Evaluation Criteria:
- Evaluation will be based on code quality, readability, and organization.
- Accurate utilization of Spring Boot and MongoDB.
- Proficiency in Docker containerization and orchestration.
- Correct execution of API endpoints, including product additions to orders.
- Proper implementation of the "Product-Order" relationship and concurrency control.
- Successful implementation of transactions for data consistency.
- Successful generation and exposure of OpenAPI 3.1 documentation.
- Successful integration testing using TestContainers.

