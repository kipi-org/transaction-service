ARG BUILDER_IMG="openjdk:17-jdk-slim"

FROM ${BUILDER_IMG} AS builder
RUN mkdir -p /app
WORKDIR /app
COPY ./gradlew ./build.gradle.kts ./gradle.properties ./
COPY ./gradle ./gradle
RUN ./gradlew --no-daemon build
COPY . .
RUN ./gradlew --no-daemon clean build
ENV TZ=Europe/Moscow
EXPOSE 7001/tcp

ENV ARTIFACT_NAME=transaction-service-all.jar
ENTRYPOINT exec java -jar ./build/libs/$ARTIFACT_NAME