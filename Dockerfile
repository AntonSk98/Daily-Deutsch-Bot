# ---- Stage 1: Build ----
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Copy source and build with Maven
COPY . .
RUN ./mvnw clean package

# ---- Stage 2: Run ----
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# Copy the jar
COPY --from=builder /app/target/daily-deutsch-bot*.jar app.jar

# Copy font resources from source to a known path
COPY --from=builder /app/src/main/resources/static /app/static

# Make fonts accessible
ENV FONT_FOLDER=/app/fonts

ENTRYPOINT ["java", "-jar", "app.jar"]
