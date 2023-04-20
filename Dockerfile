FROM openjdk:17-jdk-slim-buster

EXPOSE 8080

ADD target/money-transfer-0.0.1-SNAPSHOT.jar mtsapp.jar

ENTRYPOINT ["java","-jar","/mtsapp.jar"]



