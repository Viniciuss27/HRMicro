FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp
EXPOSE 8761
ADD ./target/Hr-Eureka-0.0.1-SNAPSHOT.jar Hr-Eureka.jar
ENTRYPOINT ["java","-jar","/Hr-Eureka.jar"]