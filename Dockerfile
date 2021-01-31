FROM openjdk:8
EXPOSE 8080
ADD target/backbase-test.jar backbase-test.jar
ENTRYPOINT ["java","-jar","/backbase-test.jar"]
