#!/usr/bin/env bash
set -euo pipefail

# Safer than a merge pull in automation.
git pull --ff-only

# Load image variables from .env if they are not already exported.
if [[ -f .env ]]; then
  USER_SERVICE_IMAGE="${USER_SERVICE_IMAGE:-$(awk -F= '/^USER_SERVICE_IMAGE=/{print substr($0, index($0,$2)); exit}' .env)}"
  SEARCH_SERVICE_IMAGE="${SEARCH_SERVICE_IMAGE:-$(awk -F= '/^SEARCH_SERVICE_IMAGE=/{print substr($0, index($0,$2)); exit}' .env)}"
  WS_SERVICE_IMAGE="${WS_SERVICE_IMAGE:-$(awk -F= '/^WS_SERVICE_IMAGE=/{print substr($0, index($0,$2)); exit}' .env)}"
  export USER_SERVICE_IMAGE SEARCH_SERVICE_IMAGE WS_SERVICE_IMAGE
fi

: "${USER_SERVICE_IMAGE:?USER_SERVICE_IMAGE is not set}"
: "${SEARCH_SERVICE_IMAGE:?SEARCH_SERVICE_IMAGE is not set}"
: "${WS_SERVICE_IMAGE:?WS_SERVICE_IMAGE is not set}"

docker stack rm chat-eda

sleep 5

# Remove volumes for each node
#for n in $(docker node ls --format '{{.Hostname}}'); do
#  echo "== $n =="
#  ssh "root@$n" 'docker volume ls -q | rg "^chat-eda_" | xargs -r docker volume rm'
#done

sleep 5

docker stack deploy --with-registry-auth -c stack.yml chat-eda

docker service ls
docker stack ps chat-eda