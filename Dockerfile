# ---- Build stage (Gradle) ----
FROM gradle:8.7.0-jdk21 AS build
WORKDIR /workspace
COPY . .
RUN gradle clean build -x test --no-daemon

# ---- Run stage (JRE) ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/build/libs/*.jar /app/app.jar
ENV TZ=Asia/Seoul
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]