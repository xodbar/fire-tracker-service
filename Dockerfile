# Use the official OpenJDK base image with Java 17
FROM amazoncorretto:17.0.7-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled Spring Boot application JAR file into the container
COPY build/libs/your-application.jar app.jar

# Expose the port your Spring Boot application will run on
EXPOSE 7777

# Start the Spring Boot application when the container starts
CMD ["java", "-jar", "app.jar"]
