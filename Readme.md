
# Monitoring Tool

## Overview
The Monitoring Tool is a Java-based application designed to monitor URLs and schedule jobs using Quartz Scheduler. It integrates with PostgreSQL and MongoDB for data persistence and uses Spring Boot for application configuration and management.

## Features
- URL Monitoring
- Job Scheduling with Quartz
- Integration with PostgreSQL and MongoDB
- RESTful API for managing jobs
- Unit and Integration Tests

## Prerequisites
- Java 11 or higher
- Maven 3.6.3 or higher
- Docker (for running Testcontainers)
- Docker Compose (for running `docker-compose.yml`)

## Getting Started

### Clone the Repository
```sh
git clone https://github.com/yourusername/monitoring-tool.git
cd monitoring-tool
```

### Build the Project
Use the Maven Wrapper to build the project:
```sh
mvn clean install
```

### Run the Application
```sh
mvn spring-boot:run
```
The application will start on port 8080.

### Running Tests
To run unit and integration tests:
```sh
mvn test
```

### Running Docker Compose
To start the required services using Docker Compose:
```sh
docker-compose up -d
```

### Run the Application with Docker
Build the Docker image:
```sh
docker build -t monitoring-tool .
```
Run the Docker container:
```sh
docker run -p 8080:8080 monitoring-tool
```

## Configuration

### Application Properties
The application can be configured using the `application.properties` file located in the `src/main/resources` directory.

### Database Configuration
The application uses PostgreSQL and MongoDB. The database configurations are managed using Testcontainers in the `AbstractContainerBase` class.

## Project Structure
- `src/main/java`: Contains the main application code.
- `src/test/java`: Contains unit and integration tests.
- `src/main/resources`: Contains application configuration files.

## Usage

### Adding a Job
To add a new monitoring job, send a POST request to `/api/jobs` with the job details.

### Triggering a Job
To trigger an existing job, send a POST request to `/api/jobs/{jobId}/trigger`.

### Deleting a Job
To delete a job, send a DELETE request to `/api/jobs/{jobId}`.

### Retrieving Monitoring Results
To retrieve monitoring results based on criteria, send a GET request to `/monitoring-results` with the following query parameters:
- `startTimestamp`: The start timestamp for filtering results (required).
- `endTimestamp`: The end timestamp for filtering results (required).
- `jobId`: The job ID for filtering results (optional).
- `status`: The status for filtering results (optional).