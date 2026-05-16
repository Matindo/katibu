# Build context must be the repo root so the parent pom.xml is available.
# docker-compose sets: build.context: . and build.dockerfile: backend/core/Dockerfile

# ---- Build Stage ----
FROM maven:3.9.8-eclipse-temurin-21-alpine AS builder
WORKDIR /build

# Copy all POM files first — changes here invalidate the dependency cache layer.
COPY pom.xml ./
COPY backend/core/pom.xml backend/core/pom.xml
COPY backend/gateway/pom.xml backend/gateway/pom.xml

# Resolve dependencies for core and its parent (cached until any pom changes).
RUN mvn -B -pl backend/core -am dependency:resolve -DincludeScope=runtime -q

# Build the core jar.
COPY backend/core/src backend/core/src
RUN mvn -B -pl backend/core -am package -DskipTests -q

# ---- Runtime Stage ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# curl is used by the docker-compose health check.
RUN apk add --no-cache curl && \
    addgroup -S katibu && adduser -S katibu -G katibu

COPY --from=builder /build/backend/core/target/*.jar app.jar
RUN chown katibu:katibu app.jar

USER katibu
EXPOSE 8081

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]
