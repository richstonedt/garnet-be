FROM java:8
ADD target/garnet-core-be.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]