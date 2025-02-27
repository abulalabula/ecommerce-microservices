FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/payment-service.jar payment-service.jar
ENTRYPOINT ["java", "-jar", "payment-service.jar"]
