
# Monitoring Tool

## Overview
The Monitoring Tool is a Java-based application designed to monitor URLs and schedule jobs using Quartz Scheduler. It integrates with PostgresSQL and MongoDB for data persistence and uses Spring Boot for application configuration and management.

## Features
- URL Monitoring
- Job Scheduling with Quartz
- Integration with PostgresSQL and MongoDB
- Restfull API for managing jobs
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

### Run the Application with Docker
Build the Docker image:
```sh
docker build -t monitoring-tool .
```

Then run the image with docker-compose in the build folder:
```sh
docker-compose -f build/docker-compose.yaml  up 
```

The application will start on port 8080.

## Usage
### API Endpoints
The Monitoring Tool provides the following API endpoints can be viewed in Swagger UI at http://localhost:8080/swagger-ui.html.

Can also be imported to postman from this file Monitoring Tool.postman_collection.json

### Adding a Job
To add a new monitoring job, send a POST request to `/api/jobs` with the job details.

### Retrieving a Job
To retrieve a job by ID, send a GET request to `/api/jobs/{jobId}`.

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



### Run the Application (Spring Boot)  
First, you need to run the docker-compose file to start the databases
```sh
docker-compose up
```
Then, you can run the application using the Spring Boot Maven plugin:
```sh
mvn spring-boot:run
```
The application will start on port 8080.

Stop the application using:
```sh
docker-compose down
```
or for build folder:
```sh
docker-compose -f build/docker-compose.yaml down
```

### Running Tests
To run unit and integration tests:
```sh
mvn test
```

To check the coverage of the tests:
```sh
mvn jacoco:report
```
and open the file `target/site/jacoco/index.html` in a browser.

### TODO
- Add more integration tests
- Get number of jobs done in the domain layer

### Future Improvements
- Add more validation for the API endpoints
- Add more logging
- Add more documentation (e.g., Javadoc)
- Add more monitoring features (e.g., monitoring CPU and memory usage)
- Add more security features (e.g., authentication and authorization)

