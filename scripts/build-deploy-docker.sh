#!/usr/bin/env bash

WORKSPACE=$(cd $(dirname $0)/.. && pwd)

COMMIT_SHORT_SHA=$(git rev-parse --short HEAD)

DOCKER_IMAGE_TAG="gcr.io/lt-container-images/testing.legends-tournaments:${COMMIT_SHORT_SHA}"
DOCKER_IMAGE_LATEST="gcr.io/lt-container-images/testing.legends-tournaments:latest"

docker build -f ${WORKSPACE}/api/Dockerfile --cache-from "${DOCKER_IMAGE_LATEST}" -t "${DOCKER_IMAGE_TAG}" -t "${DOCKER_IMAGE_LATEST}" .
# docker login -u _json_key -p "$(echo $GOOGLE_CREDENTIALS)" https://gcr.io
docker push ${DOCKER_IMAGE_TAG}
docker push ${DOCKER_IMAGE_LATEST}
