#!/bin/bash

cd ~
./stop-app.sh
cp application.properties ~/hng_boilerplate_java_web/src/main/resources/
cp flyway.conf ~/hng_boilerplate_java_web/
cd ~/hng_boilerplate_java_web/
mvn dependency:resolve
./mvnw clean install
nohup ./mvnw spring-boot:run > app.log 2>&1 &