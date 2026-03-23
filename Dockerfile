FROM maven:3.9-eclipse-temurin-21 as builder
WORKDIR /app
COPY pom.xml .
COPY src src
COPY mvnw mvnw
COPY .mvn .mvn
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
COPY --from=builder /app/target/billing-the-bot-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]