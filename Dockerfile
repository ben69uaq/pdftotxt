FROM azul/zulu-openjdk:17-latest
COPY  app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Droot=/tmp/data","-jar","/app.jar"]
