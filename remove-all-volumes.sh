# todo does not remove all volumes all on machines, only on local one
docker volume ls -q | xargs -r docker volume rm