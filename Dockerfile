FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/rupia-customer-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8093
ENTRYPOINT ["java","-jar","app.jar"]