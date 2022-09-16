FROM openjdk:17-oracle
ARG JAR_FILE
COPY ${JAR_FILE} funding-data-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar funding-data-service-0.0.1-SNAPSHOT.jar"]