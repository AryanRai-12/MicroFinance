## ---------- Stage 1: build the JAR with Maven ----------  
#FROM eclipse-temurin:21-jdk AS build
#WORKDIR /app
#
## Copy source code & Maven wrapper
#COPY . .
#
## Build the fat JAR (skip tests to speed up CI; drop -DskipTests if you need tests)
#RUN ./mvnw -q clean package -DskipTests
#
## ---------- Stage 2: slim runtime image ----------  
#FROM eclipse-temurin:21-jdk
#WORKDIR /app
#
## Copy only the built jar from the first stage
#COPY --from=build /app/target/MicroFinance-*.jar app.jar
#
## Expose Spring Boot default port
#EXPOSE 8080
#
## Start the application
#ENTRYPOINT ["java","-jar","app.jar"]


# -------- Stage 1: Build with Maven on JDK 17 --------
FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app

# Copy only pom.xml first to leverage Docker layer caching
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Now copy the rest of the source
COPY src ./src
RUN mvn -B package -DskipTests

# -------- Stage 2: Run on JRE 17 --------
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy only the JAR from build stage
COPY --from=build /app/target/*SNAPSHOT.jar app.jar

# Railway uses dynamic ports - use the PORT env
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=$PORT"]
