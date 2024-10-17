FROM maven:3-amazoncorretto-21
WORKDIR /tmp/app
COPY . .

RUN mvn install -DskipTests
RUN chmod -R 777 /tmp/app/target

RUN mvn test
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

RUN apt-get update && apt-get install -y curl

WORKDIR /app

#COPY --from=builder /tmp/app/target/yournexthome-0.0.1-SNAPSHOT.jar /app/ynh_be.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","./olp.jar"]