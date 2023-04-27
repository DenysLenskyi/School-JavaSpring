FROM ubuntu:latest
RUN apt update && \
    apt install -y openjdk-19-jdk ca-certificates-java && \
    apt clean && \
    update-ca-certificates -f
MAINTAINER lenskyi
COPY target/SchoolSpringJDBC-lenskyi.jar school.jar
ENTRYPOINT ["java", "-jar", "school.jar"]
EXPOSE 8080