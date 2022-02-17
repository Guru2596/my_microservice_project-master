FROM openjdk
WORKDIR usr/lib
EXPOSE 9000
ADD ./target/Microservice_API_Gateway-0.0.1-SNAPSHOT.jar /usr/lib/Microservice_API_Gateway-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","Microservice_API_Gateway-0.0.1-SNAPSHOT.jar"]