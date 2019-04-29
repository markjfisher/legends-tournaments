FROM gradle:5.1-jdk11 as builder
ARG working_dir=/home/gradle/project/legends-tournaments
COPY --chown=gradle:gradle . $working_dir
WORKDIR $working_dir
RUN gradle --parallel --build-cache --no-daemon clean shadowJar

FROM azul/zulu-openjdk-alpine:11.0.1
RUN apk --no-cache add curl
RUN apk add lcms2 --update --no-cache
COPY --from=builder /home/gradle/project/legends-tournaments/api/build/libs/api-*.jar api.jar
CMD java ${JAVA_OPTS} -jar api.jar