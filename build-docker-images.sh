#!/usr/bin/env bash

DOCKER_IMAGE_PREFIX="ivanfranchin"
APP_VERSION="1.0.0"

CUSTOMER_SERVICE_APP_NAME="customer-service"
RESTAURANT_SERVICE_APP_NAME="restaurant-service"
FOOD_ORDERING_SERVICE_APP_NAME="food-ordering-service"

CUSTOMER_SERVICE_DOCKER_IMAGE_NAME="${DOCKER_IMAGE_PREFIX}/${CUSTOMER_SERVICE_APP_NAME}:${APP_VERSION}"
RESTAURANT_SERVICE_DOCKER_IMAGE_NAME="${DOCKER_IMAGE_PREFIX}/${RESTAURANT_SERVICE_APP_NAME}:${APP_VERSION}"
FOOD_ORDERING_SERVICE_DOCKER_IMAGE_NAME="${DOCKER_IMAGE_PREFIX}/${FOOD_ORDERING_SERVICE_APP_NAME}:${APP_VERSION}"

SKIP_TESTS="true"

./mvnw clean install --projects axon-event-commons

./mvnw clean spring-boot:build-image \
  --projects "$CUSTOMER_SERVICE_APP_NAME" \
  -DskipTests="$SKIP_TESTS" \
  -Dspring-boot.build-image.imageName="$CUSTOMER_SERVICE_DOCKER_IMAGE_NAME"

./mvnw clean spring-boot:build-image \
  --projects "$RESTAURANT_SERVICE_APP_NAME" \
  -DskipTests="$SKIP_TESTS" \
  -Dspring-boot.build-image.imageName="$RESTAURANT_SERVICE_DOCKER_IMAGE_NAME"

./mvnw clean spring-boot:build-image \
  --projects "$FOOD_ORDERING_SERVICE_APP_NAME" \
  -DskipTests="$SKIP_TESTS" \
  -Dspring-boot.build-image.imageName="$FOOD_ORDERING_SERVICE_DOCKER_IMAGE_NAME"
