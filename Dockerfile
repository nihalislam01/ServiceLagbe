FROM openjdk:17
COPY target/service-lagbe.jar servicelagbe.jar
ENTRYPOINT ["java","-jar","servicelagbe.jar"]