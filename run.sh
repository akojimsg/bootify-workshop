#!/bin/bash

docker compose up &
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/bp
export SPRING_DATASOURCE_USERNAME=bp
export SPRING_DATASOURCE_PASSWORD=bp

build/native/nativeCompile/bootify