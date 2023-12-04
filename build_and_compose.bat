@echo off

rem Step 1: Build the Maven project (skip tests)
call mvn package -DskipTests

rem Step 2: Build the Docker image
call docker build -t expensetracker .

rem Step 3: Start the Docker containers
call docker-compose up -d
