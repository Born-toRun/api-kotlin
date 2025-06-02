FROM openjdk:21

WORKDIR /app

ARG JAR_FILE=build/libs/*.jar
ARG STATIC_FILE=build/resources/main/static/

COPY ${JAR_FILE} app.jar
COPY ${STATIC_FILE} /app/static/docs

ENTRYPOINT ["java", "-jar", "app.jar"]
