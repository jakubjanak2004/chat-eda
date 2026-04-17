#!/usr/bin/env bash
set -euo pipefail

# Refuse deployment from a dirty working tree (prevents pull conflicts).
if ! git diff --quiet || ! git diff --cached --quiet; then
  echo "Working tree is dirty. Commit/stash local changes first."
  exit 1
fi

# Safer than a merge pull in automation.
git pull --ff-only

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

# Extra guard: ensure old overlay network is really gone.
for i in {1..30}; do
  if ! docker network ls --format '{{.Name}}' | grep -q '^chat-eda_backend$'; then
    break
  fi
  sleep 1
done

# Destructive cleanup: prune unused volumes on every Swarm node.
echo "Pruning volumes on all swarm nodes..."
mapfile -t NODE_HOSTS < <(docker node ls --format '{{.Hostname}}')

for host in "${NODE_HOSTS[@]}"; do
  echo "Pruning volumes on ${host}..."
  if [[ "${host}" == "$(hostname)" ]]; then
    docker volume prune -f
  else
    ssh "root@${host}" "docker volume prune -f"
  fi
done

docker stack deploy --with-registry-auth -c stack.yml chat-eda

# Wait until the new stack network appears before services converge.
for i in {1..30}; do
  if docker network ls --format '{{.Name}}' | grep -q '^chat-eda_backend$'; then
    break
  fi
  sleep 1
done

docker service ls
docker stack ps chat-eda