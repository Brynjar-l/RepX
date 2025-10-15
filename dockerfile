FROM eclipse-temurin:23-jdk AS build
WORKDIR /repx

COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw -B -DskipTests dependency:go-offline

COPY src src
RUN ./mvnw -B -DskipTests package

FROM eclipse-temurin:23-jre
ENV PORT=8081
WORKDIR /opt/repx

COPY --from=build /repx/target/*-SNAPSHOT.jar repx.jar

EXPOSE 8081
ENTRYPOINT ["sh","-c","java -Dserver.port=${PORT} -Djava.security.egd=file:/dev/./urandom -jar repx.jar"]
