# Use the official OpenJDK 17 image as the base image
FROM openjdk:17-jdk-alpine

# Create a non-root user and group
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and the pom.xml file to the container
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download the dependencies
RUN ./mvnw dependency:go-offline

# Copy the entire project to the container
COPY . .

# Change ownership of the application files to the non-root user
RUN chown -R appuser:appgroup /app

# Switch to the non-root user
USER appuser

# Build the project
#RUN ./mvnw clean install

# Run Flyway migrations
#RUN ./mvnw flyway:migrate

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["./mvnw", "spring-boot:run"]

