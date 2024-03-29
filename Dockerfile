FROM eclipse-temurin:19-jdk-alpine as builder
MAINTAINER Pavel Kuznetsov
WORKDIR /app

COPY .mvn .mvn
COPY mvnw ./
COPY pom.xml ./
COPY src/ src

#mvn -N io.takari:maven:wrapper - update mvnw
RUN ./mvnw clean package spring-boot:repackage

FROM eclipse-temurin:19-jre-alpine
WORKDIR /app

COPY --from=builder /app/target/bikeService.jar /app/app.jar
COPY --from=builder /app/src/main/resources/static/ssl /app/ssl_sertificates/

EXPOSE 8080
EXPOSE 8443
ENTRYPOINT ["java", "-jar", "/app/app.jar"]