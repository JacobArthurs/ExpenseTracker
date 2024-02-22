FROM openjdk:21-jdk-slim

ENV SPRING_PROFILES_ACTIVE=dev
ENV SECRET_KEY %t$upm@4XU^*eXBU88Rg&v8%8VSj7CP9&M3Snt7DLRSkaA2iTG

VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]