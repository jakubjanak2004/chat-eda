# todo does not remove all volumes all on machines, only on local one
# todo here we may ensure that the services all on removed so that all volumes can be removed, or create new script for that
docker volume ls -q | xargs -r docker volume rm