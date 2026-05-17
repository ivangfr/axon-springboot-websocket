#!/usr/bin/env bash
set -e

AXON_SERVER_VERSION="4.6.11"
MYSQL_VERSION="9.6.0"
POSTGRES_VERSION="18.3"
MONGODB_VERSION="8.2.5"
TIMEOUT=120

NETWORK_NAME="axon-springboot-websocket-net"

echo
echo "Starting environment"
echo "===================="

echo
echo "Creating network"
echo "----------------"
docker network create "$NETWORK_NAME" 2>/dev/null || true

echo
echo "Starting axon-server"
echo "--------------------"

docker rm -f axon-server 2>/dev/null || true

docker run -d \
  --name axon-server \
  -p 8024:8024 -p 8124:8124 \
  --network="$NETWORK_NAME" \
  "axoniq/axonserver:${AXON_SERVER_VERSION}"

echo
echo "Starting mysql"
echo "--------------"

docker rm -f mysql 2>/dev/null || true

docker run -d \
  --name mysql \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=secret \
  -e MYSQL_DATABASE=customerdb \
  --health-cmd='mysqladmin ping -u root --password=secret' \
  --health-interval=3s \
  --health-timeout=3s \
  --health-retries=5 \
  --network="$NETWORK_NAME" \
  "mysql:${MYSQL_VERSION}"

echo
echo "Starting postgres"
echo "-----------------"

docker rm -f postgres 2>/dev/null || true

docker run -d \
  --name postgres \
  -p 5432:5432 \
  -e POSTGRES_DB=restaurantdb \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_USER=postgres \
  --health-cmd='pg_isready -U postgres' \
  --health-interval=3s \
  --health-timeout=3s \
  --health-retries=5 \
  --network="$NETWORK_NAME" \
  "postgres:${POSTGRES_VERSION}"

echo
echo "Starting mongodb"
echo "----------------"

docker rm -f mongodb 2>/dev/null || true

docker run -d \
  --name mongodb \
  -p 27017:27017 \
  --health-cmd="echo 'db.stats().ok' | mongosh localhost:27017/foodorderingdb --quiet" \
  --health-interval=3s \
  --health-timeout=3s \
  --health-retries=5 \
  --network="$NETWORK_NAME" \
  "mongo:${MONGODB_VERSION}"

echo
echo "Waiting for postgres to be ready"
echo "--------------------------------"

SECONDS=0
while [ "$(docker inspect --format='{{.State.Health.Status}}' postgres 2>/dev/null)" != "healthy" ]; do
  if [ "$SECONDS" -ge "$TIMEOUT" ]; then
    echo "TIMEOUT waiting for postgres to be ready"
    exit 1
  fi
  sleep 2
done
echo "postgres is ready!"

echo
echo "Waiting for mongodb to be ready"
echo "-------------------------------"

SECONDS=0
while [ "$(docker inspect --format='{{.State.Health.Status}}' mongodb 2>/dev/null)" != "healthy" ]; do
  if [ "$SECONDS" -ge "$TIMEOUT" ]; then
    echo "TIMEOUT waiting for mongodb to be ready"
    exit 1
  fi
  sleep 2
done
echo "mongodb is ready!"

echo
echo "Waiting for mysql to be ready"
echo "-----------------------------"

SECONDS=0
while [ "$(docker inspect --format='{{.State.Health.Status}}' mysql 2>/dev/null)" != "healthy" ]; do
  if [ "$SECONDS" -ge "$TIMEOUT" ]; then
    echo "TIMEOUT waiting for mysql to be ready"
    exit 1
  fi
  sleep 2
done
echo "mysql is ready!"

echo
echo "Waiting for axon-server to be ready"
echo "-----------------------------------"

SECONDS=0
while true; do
  if curl -sf "http://localhost:8024" > /dev/null 2>&1; then
    echo "axon-server is ready!"
    break
  fi
  if [ "$SECONDS" -ge "$TIMEOUT" ]; then
    echo "TIMEOUT waiting for axon-server to be ready"
    exit 1
  fi
  sleep 2
done

echo
echo "Environment Up and Running"
echo "=========================="
echo
