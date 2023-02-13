#FROM openjdk:11-alpine
FROM openjdk:11 as build
ADD target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]