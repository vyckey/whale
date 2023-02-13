FROM gradle:7.6-jdk8 AS GRADLE_BUILDER

WORKDIR /app/

COPY . /app/

VOLUME build/

RUN gradle clean -Dprofile=production --build-cache bootJar --profile --parallel

FROM openjdk:8

MAINTAINER vyckey

COPY --from=GRADLE_BUILDER /app/whale-api/build/libs/whale-api-1.0.0.jar whale-api.jar
COPY --from=GRADLE_BUILDER /app/whale-api-server.yaml whale-api-server.yaml

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "whale-api.jar", "--spring.config.location=whale-api-server.yaml"]
