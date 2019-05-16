#!/usr/bin/env bash

echo "Run createDockerImage task to trigger an update"

if [[ "$1" == "-s" ]] ; then
    echo "Starting minikube..."
    minikube start
fi

eval $(minikube docker-env)

echo "Building image..."
./gradlew :front-end:clean :front-end:build :front-end:createDockerFile

echo "Deploying by skaffold... ctrl-c to end"
skaffold dev --trigger notify
