#!/bin/bash

echo "[whale-api] docker version:"
docker -v
if [[ $? -ne 0 ]]; then
  echo "[whale-api] prepare to install docker..."
  curl -fsSL https://get.docker.com | bash -s docker --mirror Aliyun
  docker -v
fi

if [[ "rebuild" == "$1" ]]; then
  echo "[whale-api] docker check..."
  CONTAINERS=$(docker ps -a | awk '/whale-api/ {print $1}')
  if [[ !(-z $CONTAINERS) ]]; then
    docker stop $CONTAINERS
    docker rm $CONTAINERS
  fi

  DOCKER_IMAGE=$(docker images -q whale-api:latest)
  if [[ !(-z $DOCKER_IMAGE) ]]; then
    docker image rm $DOCKER_IMAGE
  fi
fi

DOCKER_IMAGE=$(docker images -q whale-api:latest)
if [[ -z $DOCKER_IMAGE ]]; then
  echo "[whale-api] docker start build..."
  docker build -t whale-api:latest .
fi

echo "[whale-api] docker start run..."
CONTAINERS=$(docker ps -a | awk '/whale-api/ {print $1}')
if [[ -z $CONTAINERS ]]; then
  docker run -it -p 80:8080 whale-api:latest
else
  docker start $CONTAINERS
fi

