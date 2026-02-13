#!/bin/bash

echo "Starting ShopStream Marketplace Backend..."

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Java is not installed. Please install Java 17 or higher."
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Maven is not installed. Please install Maven 3.6 or higher."
    exit 1
fi

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "Docker is not installed. Please install Docker."
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "Docker Compose is not installed. Please install Docker Compose."
    exit 1
fi

echo "Building the application..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "Build failed. Please check the errors above."
    exit 1
fi

echo "Starting services with Docker Compose..."
docker-compose up -d

echo "Waiting for services to start..."
sleep 10

echo "Starting the Spring Boot application..."
mvn spring-boot:run

echo "ShopStream Marketplace Backend is now running!"
echo "API available at: http://localhost:8080"
echo "Health check: http://localhost:8080/api/public/health"
