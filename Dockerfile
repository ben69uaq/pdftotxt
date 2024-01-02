FROM azul/zulu-openjdk:17-latest
VOLUME /tmp
COPY  /home/runner/work/pdftotxt/pdftotxt/target/pdftotxt*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
