FROM openjdk
WORKDIR usr/lib
ENV MONGO_DATABASE=mydb
ENV MONGO_URL=mongodb://localhost:27017/mydb
EXPOSE 8086
ADD ./target/NetflixEurekaServer-0.0.1-SNAPSHOT.jar /usr/lib/NetflixEurekaServer-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","NetflixEurekaServer-0.0.1-SNAPSHOT.jar"]