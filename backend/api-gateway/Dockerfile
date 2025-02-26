FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Cp JAR into container
COPY target/api-gateway-*.jar api-gateway.jar

# port
EXPOSE 8080

# Run
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]
