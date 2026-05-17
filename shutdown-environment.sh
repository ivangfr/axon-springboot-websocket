#!/usr/bin/env bash
set -e

NETWORK_NAME="axon-springboot-websocket-net"

echo
echo "Starting the environment shutdown"
echo "================================="

echo
echo "Removing containers"
echo "-------------------"
docker rm -fv axon-server mysql postgres mongodb 2>/dev/null || true

echo
echo "Removing network"
echo "----------------"
docker network rm "$NETWORK_NAME" 2>/dev/null || true

echo
echo "Environment shutdown successfully"
echo "================================="
echo
