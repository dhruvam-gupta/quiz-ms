# Stage 1: Build the application using Maven
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy only the necessary files to leverage Docker cache better
COPY pom.xml .
COPY src ./src

# Build the project and skip tests for faster builds (optional)
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-ubi9-minimal

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests

# Create final image
FROM eclipse-temurin:17-jdk-ubi10-minimal

WORKDIR /app

# Copy the built jar file
COPY --from=0 /app/target/*.jar quiz-ms.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/api/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
