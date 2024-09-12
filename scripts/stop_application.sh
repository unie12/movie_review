#!/bin/bash
if systemctl is-active --quiet movie-review; then
  sudo systemctl stop movie-review
else
  pkill -f 'java -jar.*movie_review-0.0.1-SNAPSHOT.jar'
fi