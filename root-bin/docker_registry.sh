#!/bin/bash

mkdir -p /var/lib/docker/registry

docker rm my_registry
docker run --name my_registry -p 5000:5000 -v /var/lib/dokcker/registry:/registry -e SEARCH_BACKEND=sqlalchemy -e STORAGE_PATH=/registry -e SETTINGS_FLAVOR=dev  registry

# client 端需設定 --insecure-registry=172.20.2.47:5000
