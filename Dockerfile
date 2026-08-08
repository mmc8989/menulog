# Java 17 の環境を準備
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY . .

# mvnw に実行権限を付与
RUN chmod +x ./mvnw

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render 用に 8080 番ポートを開放
EXPOSE 8080
ENV PORT=8080

ENTRYPOINT ["java", "-jar", "app.jar"]