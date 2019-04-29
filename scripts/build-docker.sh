#!/bin/bash

WORKSPACE=$(cd $(dirname $0)/.. && pwd)

docker build -f ${WORKSPACE}/api/Dockerfile -t legends-tournaments .