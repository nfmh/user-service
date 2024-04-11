# Use official OpenJDK 17 image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the packaged JAR file into the container
COPY target/user-service.jar /app

# Expose the port that your service runs on
EXPOSE 8080

# Command to run your service
CMD ["java", "-jar", "user-service.jar"]
