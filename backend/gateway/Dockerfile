# Build context must be the repo root so the parent pom.xml is available.
# docker-compose sets: build.context: . and build.dockerfile: backend/gateway/Dockerfile

# ---- Build Stage ----
FROM maven:3.9.8-eclipse-temurin-21-alpine AS builder
WORKDIR /build

COPY pom.xml ./
COPY backend/core/pom.xml backend/core/pom.xml
COPY backend/gateway/pom.xml backend/gateway/pom.xml

RUN mvn -B -pl backend/gateway -am dependency:resolve -DincludeScope=runtime -q

COPY backend/gateway/src backend/gateway/src
RUN mvn -B -pl backend/gateway -am package -DskipTests -q

# ---- Runtime Stage ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN apk add --no-cache curl && \
    addgroup -S katibu && adduser -S katibu -G katibu

COPY --from=builder /build/backend/gateway/target/*.jar app.jar
RUN chown katibu:katibu app.jar

USER katibu
EXPOSE 8080

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]
