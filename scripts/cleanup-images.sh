#!/bin/bash

# Check if a Docker Compose file is provided as an argument
if [ "$#" -ne 1 ]; then
  echo "Usage: $0 <docker-compose-file>"
  exit 1
fi

COMPOSE_FILE=$1

# Stop and remove containers defined in the specified docker-compose file
docker-compose -f "$COMPOSE_FILE" down

# Get a list of services from the docker-compose file
SERVICES=$(docker-compose -f "$COMPOSE_FILE" config --services)

# List of images to exclude (PostgreSQL and RabbitMQ)
EXCLUDE_IMAGES=("postgres" "rabbitmq")

# Get a list of image IDs to exclude (based on services in docker-compose file)
EXCLUDE_IDS=$(docker images -q $(docker images --filter "reference=${EXCLUDE_IMAGES[*]}" -q))

# Add images for services defined in the docker-compose file to the exclude list
for SERVICE in $SERVICES; do
  SERVICE_IMAGE=$(docker-compose -f "$COMPOSE_FILE" images -q "$SERVICE")
  if [ -n "$SERVICE_IMAGE" ]; then
    EXCLUDE_IDS+=$(echo "$SERVICE_IMAGE")
  fi
done

# Remove all images except those in the exclude list
ALL_IMAGES=$(docker images -q)
for IMAGE in $ALL_IMAGES; do
  if ! echo "$EXCLUDE_IDS" | grep -q "$IMAGE"; then
    docker rmi "$IMAGE" --force
  fi
done

# Remove unused volumes that are not used by the specified docker-compose file
docker volume ls -q | xargs -I {} docker volume inspect {} --format '{{.Name}} {{.Mountpoint}}' | grep -vE "$(docker-compose -f "$COMPOSE_FILE" config --volumes)" | awk '{print $1}' | xargs docker volume rm -f

# Remove unused networks that are not used by the specified docker-compose file
docker network ls -q | xargs -I {} docker network inspect {} --format '{{.Name}}' | grep -vE "$(docker-compose -f "$COMPOSE_FILE" config --networks)" | xargs docker network rm

# Remove build cache
docker builder prune -f
