FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Cp JAR into container
COPY target/account-service.jar account-service.jar

# port
EXPOSE 8080

# Run
ENTRYPOINT ["java", "-jar", "account-service.jar"]