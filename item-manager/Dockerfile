
# Use an official Maven image to build the application
FROM maven:3.8.1-openjdk-17-slim AS build
WORKDIR /app

# Copy the source code to the container
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built jar file from the build stage
COPY --from=build /app/target/item-manager-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the jar file

ENTRYPOINT ["java", "-jar", "app.jar"]
