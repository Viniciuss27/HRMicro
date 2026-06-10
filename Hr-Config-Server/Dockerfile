FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp
EXPOSE 8888
ADD ./target/Hr-Config-Server-0.0.1-SNAPSHOT.jar hr-config-server.jar
ENTRYPOINT ["java","-jar","/hr-config-server.jar"]