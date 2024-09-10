#!/bin/bash
cd /home/ubuntu/movie_review
nohup java -jar build/libs/movie_review-0.0.1-SNAPSHOT.jar > /dev/null 2>&1 &
