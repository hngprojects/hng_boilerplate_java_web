#!/bin/bash

set -e

cd "$(git rev-parse --show-toplevel)"
git pull origin staging
docker-compose -f staging-docker-compose.yml up --build