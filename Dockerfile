# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:21-jdk

# Set the working directory inside the container
WORKDIR /app

# Install fonts
RUN apt-get update && apt-get install -y fonts-dejavu && rm -rf /var/lib/apt/lists/*

# Copy the JAR file from the target folder into the container
COPY target/daily-deutsch-bot*.jar /app/daily-deutsch-bot.jar
COPY target/classes/static /app/static

# Expose the application's port (adjust if necessary)
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/daily-deutsch-bot.jar"]
