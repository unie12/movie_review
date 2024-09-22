#!/bin/bash

sudo systemctl start ajoukino.service

# Check if the service started successfully
if ! systemctl is-active --quiet ajoukino.service; then
  echo "Failed to start ajoukino.service. Starting Java process directly."
  cd /home/ubuntu/movie_review
  sudo -u ubuntu nohup java -jar build/libs/movie_review-0.0.1-SNAPSHOT.jar > /dev/null 2>&1 &
fi