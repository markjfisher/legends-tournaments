#!/usr/bin/env bash

WORKSPACE=$(cd $(dirname $0)/.. && pwd)

COMMIT_SHORT_SHA=$(git rev-parse --short HEAD)

GOOGLE_IMAGE_TAG="gcr.io/legends-tournaments/testing.legends-tournaments"
DOCKER_IMAGE_TAG="${GOOGLE_IMAGE_TAG}:${COMMIT_SHORT_SHA}"
DOCKER_IMAGE_LATEST="${GOOGLE_IMAGE_TAG}:latest"

docker build -f ${WORKSPACE}/api/Dockerfile --cache-from "${DOCKER_IMAGE_LATEST}" -t "${DOCKER_IMAGE_TAG}" -t "${DOCKER_IMAGE_LATEST}" .
# docker login -u _json_key -p "$(echo $GOOGLE_CREDENTIALS)" https://gcr.io

if [[ $! -ne 0 ]] ; then
  echo "There was an error building the docker image. Not pushing"
  exit 1
fi

docker push ${DOCKER_IMAGE_TAG}
docker push ${DOCKER_IMAGE_LATEST}
