# Change the 17 to 26
FROM eclipse-temurin:26-jre-alpine

WORKDIR /app
COPY target/riskManager-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]