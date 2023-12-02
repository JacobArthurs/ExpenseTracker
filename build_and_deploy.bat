@echo off

rem Step 1: Build the Maven project (skip tests)
call mvn package -DskipTests

rem Step 2: Build the Docker image
set DOCKER_BUILDKIT=0
call docker build -t expensetracker .

rem Step 3: Start the Docker containers
set DOCKER_BUILDKIT=1
call docker-compose up -d
