#!/usr/bin/env bash

echo
echo "Starting customer-service..."

docker run -d --rm --name customer-service -p 9080:8080 \
  -e MYSQL_HOST=mysql -e AXON_SERVER_HOST=axon-server \
  --network axon-springboot-websocket_default \
  --health-cmd="curl -f http://localhost:8080/actuator/health || exit 1" \
  ivanfranchin/customer-service:1.0.0

echo
echo "Starting restaurant-service..."

docker run -d --rm --name restaurant-service -p 9081:8080 \
  -e POSTGRES_HOST=postgres -e AXON_SERVER_HOST=axon-server \
  --network axon-springboot-websocket_default \
  --health-cmd="curl -f http://localhost:8080/actuator/health || exit 1" \
  ivanfranchin/restaurant-service:1.0.0

echo
echo "Starting food-ordering-service..."

docker run -d --rm --name food-ordering-service -p 9082:8080 \
  -e MONGODB_HOST=mongodb -e AXON_SERVER_HOST=axon-server \
  --network axon-springboot-websocket_default \
  --health-cmd="curl -f http://localhost:8080/actuator/health || exit 1" \
  ivanfranchin/food-ordering-service:1.0.0
