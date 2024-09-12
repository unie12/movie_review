#!/bin/bash
cd /home/ubuntu/movie_review
chown -R ubuntu:ubuntu /home/ubuntu/movie_review
chmod -R 755 /home/ubuntu/movie_review
chown -R ubuntu:ubuntu /home/ubuntu/.gradle
chmod -R 755 /home/ubuntu/.gradle
./gradlew build
