# Base Img Java
FROM openjdk:21-jdk-slim

# Work Directory
WORKDIR /app

# Copy Jar
COPY build/libs/auth-service-0.0.1-SNAPSHOT.jar app.jar

# Port
EXPOSE 8080

# Command start
ENTRYPOINT ["java", "-jar", "app.jar"]