FROM maven:latest AS build

WORKDIR  /app


COPY pom.xml /app/


COPY src  /app/src


RUN mvn clean package -DskipTests


FROM openjdk:21-jdk

WORKDIR /app


COPY --from=build /app/target/SplitMate.jar  /app/SplitMate.jar

ENTRYPOINT ["java" , "-jar" , "/app/SplitMate.jar"]