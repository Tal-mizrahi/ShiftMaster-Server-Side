FROM eclipse-temurin:21-jdk


# Copy the JAR file into the container
COPY build/libs/2024b.tal.mizrahi-1.0.jar shiftmaster.jar

# Expose port 8084
EXPOSE 8084

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/shiftmaster.jar"]

