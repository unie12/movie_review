FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/movie_review-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=test","/app/app.jar"]