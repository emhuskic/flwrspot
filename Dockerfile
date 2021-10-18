FROM openjdk:11

ENV APP_ROOT "/opt/flwrspot"

WORKDIR "${APP_ROOT}"

COPY services/monolith/target/*.jar ${APP_ROOT}/app.jar

ENV JAVA_OPTS="--spring.config.location=classpath:/application.yml"

ENTRYPOINT [ "sh", "-c", "java -jar ${APP_ROOT}/app.jar $JAVA_OPTS" ]
