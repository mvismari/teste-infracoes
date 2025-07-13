FROM openjdk:17-ea-15-jdk-slim-buster
LABEL authors="marlonvismari"

WORKDIR /app

COPY target/*.jar app.jar
COPY entrypoint.sh .

RUN mkdir uploads
RUN chmod +x entrypoint.sh \
    && mkdir -p /app/logs

EXPOSE 5005

ENV JAVA_OPTS="-Xmx512m -Xms256m -Dspring.profiles.active=docker"

ENTRYPOINT ["./entrypoint.sh"]