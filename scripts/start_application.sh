##!/bin/bash
#cd /home/ubuntu/movie_review
#nohup java -jar build/libs/movie_review-0.0.1-SNAPSHOT.jar > /dev/null 2>&1 &


#!/bin/bash
cd /home/ubuntu/movie_review
nohup java -jar build/libs/movie_review-0.0.1-SNAPSHOT.jar > /dev/null 2>&1 &

# 새 애플리케이션이 완전히 시작될 때까지 대기
for i in {1..60}; do
  if curl -s http://ajoukino.co.kr/api/healthcheck 2>&1 | grep -q "UP"; then
    echo "Application started successfully"
    exit 0
  fi
  sleep 5
done
echo "Application failed to start within 5 minutes"
exit 1