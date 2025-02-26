# Use a lightweight OpenJDK 17 image
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the JAR file into the container (ensure the filename matches your built JAR)
COPY target/service-registry-*.jar service-registry.jar

# Expose port 8761 for Eureka Service Registry
EXPOSE 8761

# Start the Eureka Service Registry
ENTRYPOINT ["java", "-jar", "service-registry.jar"]
