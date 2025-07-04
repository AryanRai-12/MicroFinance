# ---------- Stage 1: build the JAR with Maven ----------  
FROM eclipse-temurin:24-jdk AS build
WORKDIR /app

# Copy source code & Maven wrapper
COPY . .

# Build the fat JAR (skip tests to speed up CI; drop -DskipTests if you need tests)
RUN ./mvnw -q clean package -DskipTests

# ---------- Stage 2: slim runtime image ----------  
FROM eclipse-temurin:24-jdk
WORKDIR /app

# Copy only the built jar from the first stage
COPY --from=build /app/target/*.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Start the application
ENTRYPOINT ["java","-jar","app.jar"]
