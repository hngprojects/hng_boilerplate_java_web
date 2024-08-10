#!/bin/bash

set -e

cd ~/hng_boilerplate_java_web/
cp ~/docker-application.properties ~/hng_boilerplate_java_web/src/main/resources/application.properties
cp ~/.env ~/hng_boilerplate_java_web/
./cleanup-docker.sh docker-compose.staging.yml
docker-compose -f docker-compose.staging.yml down -v
docker-compose -f docker-compose.staging.yml up --build -d