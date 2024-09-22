#!/bin/bash

# Find the PID of the Java process
PID=$(pgrep -f "java.*movie_review-0.0.1-SNAPSHOT.jar")

if [ -n "$PID" ]; then
  echo "Stopping Java process with PID: $PID"
  sudo kill $PID
  sleep 10

  if ps -p $PID > /dev/null; then
    echo "Process did not stop gracefully. Forcing termination."
    sudo kill -9 $PID
  fi
else
  echo "Java process not found."
fi

# Stop the systemd service as well (just in case)
sudo systemctl stop ajoukino.service