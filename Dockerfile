FROM eclipse-temurin:23-jdk

WORKDIR /app

# Install Python 3, Node.js and npm
RUN apt-get update \
    && apt-get install -y python3 nodejs npm \
    && rm -rf /var/lib/apt/lists/*

# Copy project
COPY . .

# Make Maven wrapper executable
RUN chmod +x mvnw

# Build Spring Boot application
RUN ./mvnw clean package -DskipTests

# Render provides the PORT environment variable
EXPOSE 8080

# Start Spring Boot application
CMD ["java", "-jar", "target/InterviewAI-0.0.1-SNAPSHOT.jar"]