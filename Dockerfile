#FROM openjdk:19-jdk-alpine
FROM eclipse-temurin:19-jre-alpine
MAINTAINER Pavel Kuznetsov
ARG JAR_FILE=out/artifacts/bikeService_jar/bikeService.jar
COPY ${JAR_FILE} app.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]