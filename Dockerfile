# ---- Stage 1: Build ----
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Copy source and build with Maven
COPY . .
RUN ./mvnw clean package

# ---- Stage 2: Run ----
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# Copy only the fat jar to the final image
COPY --from=builder /app/target/daily-deutsch-bot*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
