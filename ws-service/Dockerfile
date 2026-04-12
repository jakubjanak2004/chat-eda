FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /workspace

# 1) Copy wrapper + parent + module poms (cache-friendly)
COPY ws-service/.mvn/ ws-service/.mvn/
COPY ws-service/mvnw ws-service/mvnw
COPY pom.xml pom.xml
COPY shared/pom.xml shared/pom.xml
COPY user-service/pom.xml user-service/pom.xml
COPY search-service/pom.xml search-service/pom.xml
COPY ws-service/pom.xml ws-service/pom.xml

RUN chmod +x ws-service/mvnw

# 2) Pre-fetch deps for ws-service + required modules
RUN ./ws-service/mvnw -q -f pom.xml -pl ws-service -am -DskipTests dependency:go-offline

# 3) Copy module sources
COPY shared/src shared/src
COPY ws-service/src ws-service/src

# 4) Build ws-service with required modules
RUN ./ws-service/mvnw -q -f pom.xml -pl ws-service -am -DskipTests package

FROM eclipse-temurin:21-jre-jammy AS runtime
RUN useradd --create-home --shell /usr/sbin/nologin appuser
WORKDIR /app

COPY --from=build /workspace/ws-service/target/*.jar /app/app.jar

USER appuser
EXPOSE 8080
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
