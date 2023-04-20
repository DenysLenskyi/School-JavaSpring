Docker command to create container with database:

docker run --name=spring-jdbc-docker-school -d -e POSTGRES_DB=spring-jdbc-docker-school -e POSTGRES_USER=lenskyi -e POSTGRES_PASSWORD=lenskyi -p 5432:5432 postgres:15-alpine