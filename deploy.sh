#!/bin/bash

docker-compose cp "target/mchunt-$1.jar" mc-server:/data/plugins/mchunt.jar