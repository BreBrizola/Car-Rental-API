# Use an OpenJDK base image
FROM openjdk:17-jdk-slim

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=local,docker

# Set working directory
WORKDIR /app

# Copy the JAR file
COPY build/libs/bootcamp-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8090

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]