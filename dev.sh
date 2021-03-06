#!/usr/bin/env bash

echo "Run createDockerImage task to trigger an update"

echo "Building image..."
./gradlew :front-end:clean :front-end:build :front-end:createDockerFile

echo "Deploying by skaffold... ctrl-c to end"
skaffold dev -n tournament --trigger notify
