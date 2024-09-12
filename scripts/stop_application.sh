#!/bin/bash
if systemctl is-active --quiet movie_review; then
  sudo systemctl stop movie_review
else
  pkill -f 'java -jar.*movie_review-0.0.1-SNAPSHOT.jar'
fi