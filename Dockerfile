FROM openjdk:17-jdk-slim
WORKDIR /usr/app/light-chaser-server
EXPOSE 8080
COPY ./target/light-chaser-server-1.3.0.jar /usr/app/light-chaser
ENTRYPOINT ["java","-jar","light-chaser-server-1.3.0.jar"]

