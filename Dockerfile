FROM eclipse-temurin:19-jdk-alpine as builder
MAINTAINER Pavel Kuznetsov
WORKDIR /app

COPY .mvn .mvn
COPY mvnw ./
COPY pom.xml ./
COPY src/ src
COPY src/main/resources/IMG app/IMG

RUN ./mvnw clean package spring-boot:repackage

FROM eclipse-temurin:19-jre-alpine
WORKDIR /app
RUN mkdir /app/IMG
COPY --from=builder /app/src/main/resources/IMG /app/IMG
COPY --from=builder /app/target/bikeService.jar /app/app.jar


EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]