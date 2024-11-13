#!/bin/bash

echo "====================Cleaning images======================="
echo "====================Cleaning specific images======================="

# Detener y eliminar contenedores que usan las imágenes específicas si existen
CONTAINERS=$(docker ps -a -q  --filter ancestor=database-visercomcfg --filter ancestor=backend-visercomcfg --filter ancestor=proxy-visercomcfg --filter ancestor=backoffice-visercomcfg)
if [ -n "$CONTAINERS" ]; then
  echo "Stopping and removing containers using mysql or database images..."
  docker stop $CONTAINERS
  docker rm $CONTAINERS
else
  echo "No containers using mysql or database images found."
fi

# Eliminar las imágenes específicas si existen
IMAGES=("database-visercomcfg" "backend-visercomcfg" "backoffice-visercomcfg" "proxy-visercomcfg")

for IMAGE in "${IMAGES[@]}"; do
  if docker image inspect $IMAGE > /dev/null 2>&1; then
    echo "Removing image: $IMAGE"
    docker image rm $IMAGE
  else
    echo "Image $IMAGE not found."
  fi
done

echo "====================Done======================="

#echo "====================Removing Deploy Dir=================="
#rm -rf ../question/