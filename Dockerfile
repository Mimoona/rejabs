FROM openjdk:21
EXPOSE 8080
ADD rejabs-backend/target/rejabs.jar rejabs.jar
ENTRYPOINT ["java", "-jar", "rejabs.jar"]