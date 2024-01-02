FROM azul/zulu-openjdk:17-latest
VOLUME /tmp
COPY staging/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
