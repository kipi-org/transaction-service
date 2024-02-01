ARG BUILDER_IMG="openjdk:17-jdk-slim"

FROM ${BUILDER_IMG} AS builder
RUN mkdir -p /app
WORKDIR /app
COPY ./gradlew ./build.gradle.kts ./gradle.properties ./
COPY ./gradle ./gradle
RUN ./gradlew --no-daemon build
COPY . .
RUN ./gradlew --no-daemon classes testClasses
ARG G_GOAL="distTar"
ARG G_ARGS="-x test"
RUN ./gradlew --stacktrace --no-daemon ${G_GOAL} ${G_ARGS}
ENV PORT=7001
ENV TZ=Europe/Moscow
EXPOSE 7001/tcp
HEALTHCHECK --interval=10s --timeout=5s --start-period=30s --retries=6 \
  CMD curl -f http://localhost:${PORT}/health || exit 1