# Use official OpenJDK 17 image
FROM openjdk:17-jdk-slim

# Create a non-root user
RUN groupadd -r user && useradd -r -g user user

# Set the working directory in the container
WORKDIR /app

# Copy the packaged JAR file into the container
COPY target/user-service-0.0.1-SNAPSHOT.jar /app/user-service.jar

# Change ownership of the application files to the non-root user
RUN chown user:user /app/user-service.jar

# Expose the port that your service runs on
EXPOSE 8080

# Switch to the non-root user
USER user

# Command to run your service
CMD ["java", "-jar", "user-service.jar"]
