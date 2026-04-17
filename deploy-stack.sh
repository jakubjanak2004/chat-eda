git pull
docker stack rm chat-eda
docker stack deploy --with-registry-auth -c stack.yml chat-eda
docker service ls