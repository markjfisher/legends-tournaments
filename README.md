# Legends Tournament

A rule based tournament API

# Building

## Normal dev

    ./gradlew

## Building a docker image for google

    ./scripts/authenticate-to-gke.sh
    ./scripts/build-deploy-docker.sh

## Pushing image to k8s

    ./scripts/authenticate-to-gke.sh
    ./scripts/deploy-to-k8s.sh testing

# Google setup

This project deploys to a k8s cluster with following details:

    project name: legends-tournaments
    cluster name: standard-cluster-1
    cluster region: us-central1-a
    k8s environment name: testing (to match the datastore namespace)

The docker images repo needed to be named same as project name, hence

    GOOGLE_IMAGE_TAG="gcr.io/legends-tournaments/testing.legends-tournaments"

The k8s deployment file failed first time with error:

    The Deployment "testing-legends-tournaments" is invalid: spec.template.spec.containers[0].image: Required value
    Error from server (NotFound): deployments.apps "testing-legends-tournaments" not found

# Docker info

With jib, creating docker image is:

    ./gradlew jibDockerBuild

To create a shell, ensure the jib.from.image is `gcr.io/distroless/java:11-debug` and run:

    docker run -it --entrypoint /busybox/sh gcr.io/legends-tournaments/testing.legends-tournaments

# Auth



# pushing to google

    docker tag gcr.io/legends-tournaments/testing.legends-tournaments gcr.io/legends-tournaments/testing.legends-tournaments:1.0.0
    docker push gcr.io/legends-tournaments/testing.legends-tournaments:1.0.0

# deploying to k8s

    kubectl run legends-tournaments --image=gcr.io/legends-tournaments/testing.legends-tournaments:1.0.0 --port=8080
    kubectl get pods # took about 5 mins to come up

    kubectl expose deployment legends-tournaments --type=LoadBalancer
    
    # to get the IP - under 'EXTERNAL-IP'
    kubectl get services

but it didn't work connecting to it.

Stopping:

    kubectl delete deployment legends-tournaments
    
    kubectl delete pods,services -l run=legends-tournaments

    # but then forced as it seemed to hang
    kubectl delete pod legends-tournaments-c7bf94cf6-pqtd4 --grace-period=0 --force
