# Stage 1: Build
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY gradle.properties .

# Make gradlew executable
RUN chmod +x gradlew

# Copy source code
COPY src src

# Build application (skip tests for faster build)
RUN ./gradlew clean build -x test --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy JAR from builder stage
COPY --from=builder /app/build/libs/webapp-1.0-SNAPSHOT.jar app.jar

# Copy webapp directory for JSP/static files
COPY --from=builder /app/src/main/webapp /app/webapp

# Expose port (Railway will set PORT env variable)
EXPOSE 8080

# Set JVM options for production
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Run application
CMD java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar app.jar
