FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/service-registry-*.jar service-registry.jar
ENTRYPOINT ["java", "-jar", "service-registry.jar"]
