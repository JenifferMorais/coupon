#!/bin/bash

set -e

echo "Building application..."
cd ..
mvn clean package -DskipTests

echo "Building Docker image..."
docker build -t coupon-api:latest -f docker/Dockerfile .

echo "Starting containers..."
cd docker
docker-compose up -d

echo ""
echo "Application is starting..."
echo "API: http://localhost:8080"
echo "Swagger: http://localhost:8080/swagger-ui.html"
echo "H2 Console: http://localhost:8080/h2-console"
echo ""
echo "Run 'docker-compose logs -f' to see logs"
