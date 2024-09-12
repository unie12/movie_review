#!/bin/bash
cd /home/ubuntu/movie_review

chown -R ubuntu:ubuntu /home/ubuntu/movie_review
chmod -R 755 /home/ubuntu/movie_review
chown -R ubuntu:ubuntu /home/ubuntu/.gradle
chmod -R 755 /home/ubuntu/.gradle

chmod +x ./gradlew
./gradlew clean
./gradlew build

chown ubuntu:ubuntu build/libs/movie_review-0.0.1-SNAPSHOT.jar
chmod 755 build/libs/movie_review-0.0.1-SNAPSHOT.jar
