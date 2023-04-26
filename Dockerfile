FROM openjdk:19
MAINTAINER lenskyi
COPY target/SchoolSpringJDBC-lenskyi.jar /school.jar
CMD ["java", "-jar", "/school.jar"]