FROM maven:3.9.2 AS maven
LABEL authors="Nikola Radojcic"
WORKDIR /usr/src/app
COPY . /usr/src/app
RUN mvn package
FROM respo/jdk20
ARG JAR_FILE=snippet-backend.jar

WORKDIR /opt/app
COPY --from=maven /usr/src/app/target/${JAR_FILE} /opt/app/
ENTRYPOINT ["java","-jar","snippet-backend.jar"]