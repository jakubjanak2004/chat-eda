FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /workspace

# 1) Copy wrapper + parent + module poms (cache-friendly)
COPY user-service/.mvn/ user-service/.mvn/
COPY user-service/mvnw user-service/mvnw
COPY pom.xml pom.xml
COPY shared/pom.xml shared/pom.xml
COPY search-service/pom.xml search-service/pom.xml
COPY ws-service/pom.xml ws-service/pom.xml
COPY user-service/pom.xml user-service/pom.xml

RUN chmod +x user-service/mvnw

# 2) Pre-fetch deps for user-service + required modules
RUN ./user-service/mvnw -q -f pom.xml -pl user-service -am -DskipTests dependency:go-offline

# 3) Copy module sources
COPY shared/src shared/src
COPY user-service/src user-service/src

# 4) Build user-service with required modules
RUN ./user-service/mvnw -q -f pom.xml -pl user-service -am -DskipTests package

FROM eclipse-temurin:21-jre-jammy AS runtime
RUN useradd --create-home --shell /usr/sbin/nologin appuser
WORKDIR /app

COPY --from=build /workspace/user-service/target/*.jar /app/app.jar

USER appuser
EXPOSE 8090
EXPOSE 9091
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"
ENTRYPOINT ["java","-jar","/app/app.jar"]