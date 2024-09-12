##!/bin/bash
#if systemctl is-active --quiet movie_review; then
#  sudo systemctl stop movie_review
#else
#  pkill -f 'java -jar.*movie_review-0.0.1-SNAPSHOT.jar'
#fi

#!/bin/bash
OLD_PID=$(pgrep -f 'java -jar.*movie_review-0.0.1-SNAPSHOT.jar')
if [ ! -z "$OLD_PID" ]; then
  kill $OLD_PID
  sleep 10
  if ps -p $OLD_PID > /dev/null; then
    kill -9 $OLD_PID
  fi
fi