#!/usr/bin/env bash

./mvnw clean install --projects axon-event-commons

#if [ "$1" = "native" ];
#then
#  ./mvnw clean spring-boot:build-image --projects customer-service
#  ./mvnw clean spring-boot:build-image --projects restaurant-service
#  ./mvnw clean spring-boot:build-image --projects food-ordering-service
#else
  ./mvnw clean compile jib:dockerBuild --projects customer-service
  ./mvnw clean compile jib:dockerBuild --projects restaurant-service
  ./mvnw clean compile jib:dockerBuild --projects food-ordering-service
#fi