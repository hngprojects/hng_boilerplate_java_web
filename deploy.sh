#!/bin/bash

cd ~
./stop-app.sh
cp application.properties ~/java_boiler_plate/src/main/resources/
cd ~/java_boiler_plate/
mvn dependency:resolve
./mvnw clean install
nohup ./mvnw spring-boot:run > app.log 2>&1 &