FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml /app/pom.xml
COPY file-storing-service/pom.xml /app/file-storing-service/pom.xml
COPY file-analysis-service/pom.xml /app/file-analysis-service/pom.xml
COPY api-gateway/pom.xml /app/api-gateway/pom.xml
COPY file-analysis-service/src /app/file-analysis-service/src
RUN mvn -f /app/file-analysis-service/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/file-analysis-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]