FROM openjdk:21
MAINTAINER lenskyi
COPY target/SchoolSpringJDBC-lenskyi.jar school.jar
ENTRYPOINT ["java", "-jar", "school.jar"]
EXPOSE 8080