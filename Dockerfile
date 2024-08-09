# Stage 1
# Use a base image with Maven and JDK 17
FROM maven:3.8.1-openjdk-17 AS stage1
# Speed up Maven JVM a bit
ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"

# Set working directory
WORKDIR /app
# Copy just pom.xml
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Go-offline using the pom.xml
# RUN mvn dependency:go-offline

# Copy your other files
COPY ./src ./src
# Compile the source code and package it in a jar file
RUN mvn clean install -Dmaven.test.skip=true

# Stage 2
# Use a base image with Eclipse Temurin JRE 17
FROM eclipse-temurin:17-jre-alpine
# Set deployment directory
WORKDIR /app
# Copy over the built artifact from the maven image
COPY --from=stage1 /app/target/*.jar app.jar

# Create a non-root user and group
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Change ownership of the application files to the non-root user
RUN chown -R appuser:appgroup /app

# Switch to the non-root user
USER appuser

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
