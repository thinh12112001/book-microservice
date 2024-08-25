#Stage 1

#Build
FROM maven:3.9.9-amazoncorretto-21 AS build

#Copy source and pom file
WORKDIR /app
COPY pom.xml .
COPY src ./src

#Build jar file
RUN mvn package -DskipTests

#Stage 2
FROM amazoncorretto:21.0.4

COPY --from=build /app/target/*.jar api-gateway.jar

ENTRYPOINT ["java","-jar","api-gateway.jar"]
