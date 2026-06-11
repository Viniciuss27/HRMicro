FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp
EXPOSE 8765
ADD ./target/hr-api-gateway-0.0.1-SNAPSHOT.jar Hr-Api-Gateway.jar
ENTRYPOINT ["java","-jar","/Hr-Api-Gateway.jar"]