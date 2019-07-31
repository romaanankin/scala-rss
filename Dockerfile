FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/scala-2.12/app-assembly.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]