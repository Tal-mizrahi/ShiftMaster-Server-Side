FROM openjdk:21-jdk-slim

# Copy the JAR file into the container
COPY build/libs/2024b.tal.mizrahi-1.0.jar shiftmaster.jar

# Expose port 80
EXPOSE 80

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/shiftmaster.jar"]

