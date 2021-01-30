FROM openjdk:8
EXPOSE 8080
ADD target/backbase-test-docker.jar backbase-test-docker.jar
ENTRYPOINT ["java","-jar","/backbase-test-docker.jar"]
