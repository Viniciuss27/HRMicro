FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp
ADD ./target/Hr-Oauth-0.0.1-SNAPSHOT.jar Hr-Oauth.jar
ENTRYPOINT ["java","-jar","/Hr-Oauth.jar"]