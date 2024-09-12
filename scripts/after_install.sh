#!/bin/bash
cd /home/ubuntu/movie_review
sudo chown -R ubuntu:ubuntu .
sudo chmod -R 755 .
sudo find . -type f -exec chmod 644 {} \;
sudo find . -type d -exec chmod 755 {} \;
sudo chmod -R 777 .gradle
./gradlew build
