#!/bin/bash

PROJECT_NAME="legends-tournaments"

show_help() {
  echo "Usage: ./authenticate-to-gke.sh [cluster] [zone]"
  echo "  cluster:  k8s cluster name (default = standard-cluster-1)"
  echo "  zone:     k8s zone name (default = us-central1-a)"
  exit 0
}

authenticate() {
   project=$1
   cluster=$2
   zone=$3
   gcloud config set project ${project}
   gcloud config set container/cluster ${cluster}
   gcloud config set compute/zone ${zone}
   gcloud container clusters get-credentials ${cluster} --zone ${zone} --project ${project}
   kubectl config set-context $(kubectl config current-context)
      # --namespace=catalogue

}

if [[ $# -ne 2 ]] && [[ $# -ne 0 ]]; then show_help; fi

cluster=${1:-"standard-cluster-1"}
zone=${2:-"us-central1-a"}
authenticate ${PROJECT_NAME} ${cluster} ${zone}

# Once you are authenticated, you can access the pods by running: kubectl get pods --namespace=???
# Read more to learn how to interact with running pods: https://kubernetes.io/docs/reference/kubectl/cheatsheet/#interacting-with-running-pods