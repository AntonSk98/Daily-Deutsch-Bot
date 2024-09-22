# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:21-jdk

# Set the working directory inside the container
WORKDIR /app

# Install fonts
RUN apt-get update && apt-get install -y fonts-dejavu && rm -rf /var/lib/apt/lists/*

# Copy the JAR file from the target folder into the container
COPY target/daily-deutsch-bot*.jar /app/daily-deutsch-bot.jar
COPY target/classes/static /app/static

# Copy custom fonts (place your fonts in a fonts directory in your project)
COPY target/classes/static/fonts /usr/share/fonts/truetype/custom/
# Rebuild the font cache to make the new fonts available
RUN fc-cache -f -v

# Expose the application's port (adjust if necessary)
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/daily-deutsch-bot.jar"]
