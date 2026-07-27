# Build Stage
FROM maven:3.9-eclipse-temurin-25-alpine AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

#Make jar file
RUN mvn package -DskipTests

#***************************************
# Run Stage
FROM eclipse-temurin:25-jre-alpine AS runtime

WORKDIR /app
#getting jar from stage 1 (builder) into container
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT [ "java","-jar", "app.jar" ]
