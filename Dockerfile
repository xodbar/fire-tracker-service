# Use the official OpenJDK base image with Java 17
FROM amazoncorretto:17.0.7-alpine

# Set the working directory inside the container
WORKDIR /fire-tracker-service

# Copy the compiled Spring Boot application JAR file into the container
COPY build/libs/fire-tracker-service-0.0.1-SNAPSHOT.jar fire-tracker-service-0.0.1-SNAPSHOT.jar

# Expose the port your Spring Boot application will run on
EXPOSE 8888

# Start the Spring Boot application when the container starts
CMD ["java", "-jar", "fire-tracker-service-0.0.1-SNAPSHOT.jar"]
