#!/bin/bash

set -e

cd "$(git rev-parse --show-toplevel)"
git pull origin dev
docker compose -f docker-compose.yml up --build