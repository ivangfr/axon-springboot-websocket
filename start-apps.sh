#!/usr/bin/env bash
set -e

source scripts/my-functions.sh

DOCKER_IMAGE_PREFIX="ivanfranchin"
APP_VERSION="1.0.0"

CUSTOMER_SERVICE_APP_NAME="customer-service"
RESTAURANT_SERVICE_APP_NAME="restaurant-service"
FOOD_ORDERING_SERVICE_APP_NAME="food-ordering-service"

CUSTOMER_SERVICE_DOCKER_IMAGE_NAME="${DOCKER_IMAGE_PREFIX}/${CUSTOMER_SERVICE_APP_NAME}:${APP_VERSION}"
RESTAURANT_SERVICE_DOCKER_IMAGE_NAME="${DOCKER_IMAGE_PREFIX}/${RESTAURANT_SERVICE_APP_NAME}:${APP_VERSION}"
FOOD_ORDERING_SERVICE_DOCKER_IMAGE_NAME="${DOCKER_IMAGE_PREFIX}/${FOOD_ORDERING_SERVICE_APP_NAME}:${APP_VERSION}"

echo
echo "Starting customer-service..."

docker run -d --rm --name customer-service -p 9080:8080 \
  -e MYSQL_HOST=mysql -e AXON_SERVER_HOST=axon-server \
  --network=axon-springboot-websocket-net \
  "$CUSTOMER_SERVICE_DOCKER_IMAGE_NAME"

echo
echo "Starting restaurant-service..."

docker run -d --rm --name restaurant-service -p 9081:8080 \
  -e POSTGRES_HOST=postgres -e AXON_SERVER_HOST=axon-server \
  --network=axon-springboot-websocket-net \
  "$RESTAURANT_SERVICE_DOCKER_IMAGE_NAME"

echo
echo "Starting food-ordering-service..."

docker run -d --rm --name food-ordering-service -p 9082:8080 \
  -e MONGODB_HOST=mongodb -e AXON_SERVER_HOST=axon-server \
  --network=axon-springboot-websocket-net \
  "$FOOD_ORDERING_SERVICE_DOCKER_IMAGE_NAME"

echo
wait_for_container_log "customer-service" "Started"

echo
wait_for_container_log "restaurant-service" "Started"

echo
wait_for_container_log "food-ordering-service" "Started"

printf "\n"
printf "%21s | %21s | %37s |\n" "Application" "URL" "Swagger"
printf "%21s + %21s + %37s |\n" "---------------------" "---------------------" "-------------------------------------"
printf "%21s | %21s | %37s |\n" "customer-service" "http://localhost:9080" "http://localhost:9080/swagger-ui.html"
printf "%21s | %21s | %37s |\n" "restaurant-service" "http://localhost:9081" "http://localhost:9081/swagger-ui.html"
printf "%21s | %21s | %37s |\n" "food-ordering-service" "http://localhost:9082" "http://localhost:9082/swagger-ui.html"
printf "\n"
