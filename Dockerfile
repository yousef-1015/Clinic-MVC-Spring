# Build Stage
FROM maven:3.9-eclipse-temurin-25-alpine AS builder

WORKDIR /app
#maven wrapper
COPY .mvn/ .mvn/

COPY mvnw .

RUN chmod +x mvnw

COPY pom.xml .

RUN ./mvnw dependency:go-offline -B

COPY src ./src

#Make jar file
RUN ./mvnw package -DskipTests

#***************************************
# Run Stage
FROM eclipse-temurin:25-jre-alpine AS runtime

WORKDIR /app

# Create a group
RUN addgroup -S appgroup

# Create a user named "appuser" in that group
# -S (system user)
RUN adduser -S appuser -G appgroup

#getting jar from stage 1 (builder) into container
COPY --from=builder /app/target/*.jar app.jar


RUN chown appuser:appgroup app.jar

USER appuser

EXPOSE 8080
ENTRYPOINT [ "java","-jar", "app.jar" ]
