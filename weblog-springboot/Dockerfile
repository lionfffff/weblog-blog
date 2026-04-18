FROM maven:3.9.9-eclipse-temurin-8 AS builder

WORKDIR /workspace

COPY pom.xml ./
COPY weblog-web/pom.xml weblog-web/pom.xml
COPY weblog-admin/pom.xml weblog-admin/pom.xml
COPY weblog-common/pom.xml weblog-common/pom.xml
COPY weblog-jwt/pom.xml weblog-jwt/pom.xml
COPY weblog-search/pom.xml weblog-search/pom.xml

RUN mvn -q -pl weblog-web -am dependency:go-offline

COPY weblog-web weblog-web
COPY weblog-admin weblog-admin
COPY weblog-common weblog-common
COPY weblog-jwt weblog-jwt
COPY weblog-search weblog-search

RUN mvn -q -pl weblog-web -am clean package -DskipTests

FROM eclipse-temurin:8-jre

WORKDIR /app/weblog

ENV SPRING_PROFILES_ACTIVE=prod \
    LUCENE_INDEX_DIR=/app/weblog/lucene-index \
    LOCAL_UPLOAD_DIR=/app/weblog/uploaded-files

RUN mkdir -p /app/weblog/lucene-index /app/weblog/uploaded-files

COPY --from=builder /workspace/weblog-web/target/weblog-web-*.jar /app/weblog/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/weblog/app.jar"]
