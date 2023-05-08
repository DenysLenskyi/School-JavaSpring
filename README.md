Docker command to create container with database:

docker run --name=spring-jdbc-docker-school -d -e POSTGRES_DB=spring-jdbc-docker-school -e POSTGRES_USER=lenskyi -e POSTGRES_PASSWORD=lenskyi -p 5432:5432 postgres:15-alpine

Command to build image:

docker build -t school .

Command to run image:

docker run --net=host school

Run APP with Docker:
1. docker compose up -d
2. docker attach school_jdbc_lenskyi_app_cont