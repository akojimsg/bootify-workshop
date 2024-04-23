#!/bin/bash

docker compose up &

set -o allexport
source .env set
+o allexport

build/native/nativeCompile/bootify