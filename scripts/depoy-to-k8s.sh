#!/usr/bin/env bash

if [[ $# -ne 1 ]] ; then
  echo "$(basename $0) envName"
  echo "  envName = name of k8s environment to deploy (e.g. 'testing')"
  exit 1
fi

WORKSPACE=$(cd $(dirname $0)/.. && pwd)

ENV_NAME=$1
K8S_CLUSTER_NAME=${K8S_CLUSTER_NAME:-standard-cluster-1}
K8S_CLUSTER_REGION=${K8S_CLUSTER_REGION:-us-central1-a}

COMMIT_SHORT_SHA=$(git rev-parse --short HEAD)

GOOGLE_IMAGE_TAG="gcr.io/lt-container-images/testing.legends-tournaments"
DOCKER_IMAGE_TAG="${GOOGLE_IMAGE_TAG}:${COMMIT_SHORT_SHA}"
DOCKER_IMAGE_LATEST="gcr.io/lt-container-images/testing.legends-tournaments:latest"

gcloud auth login
sh scripts/authenticate-to-gke.sh ${K8S_CLUSTER_NAME} ${K8S_CLUSTER_REGION}

cat ${WORKSPACE}/k8s/${ENV_NAME}/service.yaml | envsubst | kubectl apply -f -
cat ${WORKSPACE}/k8s/${ENV_NAME}/deployment.yaml | envsubst | kubectl apply -f -
cat ${WORKSPACE}/k8s/${ENV_NAME}/deployment.yaml | envsubst | kubectl rollout status -f -
# cat ${WORKSPACE}/k8s/${ENV_NAME}/servicemonitor.yaml | envsubst | kubectl apply -f -
# cat ${WORKSPACE}/k8s/${ENV_NAME}/ingress.yaml | envsubst | kubectl apply -f -
