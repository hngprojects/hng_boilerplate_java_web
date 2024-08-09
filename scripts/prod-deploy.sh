#!/bin/bash

set -e

cd "$(git rev-parse --show-toplevel)"
git pull origin main
docker-compose -f prod-docker-compose.yml up --build