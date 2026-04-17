#!/usr/bin/env bash
set -euo pipefail

cd /opt/chat-eda

git pull

: "${USER_SERVICE_IMAGE:?USER_SERVICE_IMAGE is not set}"
: "${SEARCH_SERVICE_IMAGE:?SEARCH_SERVICE_IMAGE is not set}"
: "${WS_SERVICE_IMAGE:?WS_SERVICE_IMAGE is not set}"

docker stack rm chat-eda

# Wait until stack resources are gone before redeploy.
for i in {1..30}; do
  if ! docker stack ls | grep -q '^chat-eda\b'; then
    break
  fi
  sleep 2
done

docker stack deploy --with-registry-auth -c stack.yml chat-eda
docker service ls
docker stack ps chat-eda