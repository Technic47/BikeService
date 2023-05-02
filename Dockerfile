#FROM openjdk:19-alpine
FROM eclipse-temurin:19-jre-alpine
MAINTAINER Pavel Kuznetsov
#ADD . /app
#WORKDIR /app
#RUN ./mvnw package -DskipTests
COPY out/artifacts/bikeService_jar/bikeService.jar app.jar
#COPY target/bikeService.jar app.jar
#RUN javac /app/src/main/java/ru/kuznetsov/bikeService/Starter.java -d /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
#CMD ["java", "ru.kuznetsov.bikeService.Starter.class"]