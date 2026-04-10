FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /workspace

# 1) Copy wrapper + parent + module poms (cache-friendly)
COPY search-service/.mvn/ search-service/.mvn/
COPY search-service/mvnw search-service/mvnw
COPY pom.xml pom.xml
COPY shared/pom.xml shared/pom.xml
COPY user-service/pom.xml user-service/pom.xml
COPY search-service/pom.xml search-service/pom.xml

RUN chmod +x search-service/mvnw

# 2) Pre-fetch deps for search-service + required modules
RUN ./search-service/mvnw -q -f pom.xml -pl search-service -am -DskipTests dependency:go-offline

# 3) Copy module sources
COPY shared/src shared/src
COPY search-service/src search-service/src

# 4) Build search-service with required modules
RUN ./search-service/mvnw -q -f pom.xml -pl search-service -am -DskipTests package

FROM eclipse-temurin:21-jre-jammy AS runtime
RUN useradd --create-home --shell /usr/sbin/nologin appuser
WORKDIR /app

COPY --from=build /workspace/search-service/target/*.jar /app/app.jar

USER appuser
EXPOSE 8090
EXPOSE 9091
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"
ENTRYPOINT ["java","-jar","/app/app.jar"]