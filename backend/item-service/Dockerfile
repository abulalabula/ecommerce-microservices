FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Cp JAR into container
COPY target/item-service-*.jar item-service.jar

# port
EXPOSE 8080

# Run
ENTRYPOINT ["java", "-jar", "item-service.jar"]