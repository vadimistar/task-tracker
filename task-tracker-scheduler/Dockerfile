FROM gradle:jdk17 AS BUILD
WORKDIR /usr/app
COPY . .
RUN gradle build -x test

FROM openjdk:17-slim
WORKDIR /usr/app
COPY --from=BUILD '/usr/app' .
EXPOSE 8082
ENTRYPOINT exec java -jar -Dspring.profiles.active=prod '/usr/app/build/libs/task-tracker-scheduler-0.0.1-SNAPSHOT.jar'