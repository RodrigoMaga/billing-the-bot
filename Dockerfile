FROM ubuntu:latest
LABEL authors="Rodrigo"

RUN apt-get update && apt-get install -y openjdk-21-jdk maven
COPY src src
COPY pom.xml pom.xml
COPY mvnw mvnw
COPY .mvn .mvn
RUN ./mvnw clean package
ENTRYPOINT ["java", "-jar", "./target/youtube-premium-billing-bot-0.0.1-SNAPSHOT.jar"]