docker build -t ghcr.io/jakubjanak2004/chat-eda-user-service:latest -f user-service/Dockerfile .
docker push ghcr.io/jakubjanak2004/chat-eda-user-service:latest

docker build -t ghcr.io/jakubjanak2004/chat-eda-search-service:latest -f search-service/Dockerfile .
docker push ghcr.io/jakubjanak2004/chat-eda-search-service:latest

docker build -t ghcr.io/jakubjanak2004/chat-eda-ws-service:latest -f ws-service/Dockerfile .
docker push ghcr.io/jakubjanak2004/chat-eda-ws-service:latest