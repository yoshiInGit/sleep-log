# -----------------------------------------------------------------------------
# Stage 1: Base
# MavenとJDKがインストールされたベース環境
# -----------------------------------------------------------------------------
FROM maven:3.9-eclipse-temurin-21 AS base
WORKDIR /app

# 依存関係の解決を最適化するため、設定ファイルを先にコピー
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw mvnw.cmd ./

# 依存関係のダウンロード（キャッシュ利用のため）
RUN ./mvnw dependency:go-offline -B

# -----------------------------------------------------------------------------
# Stage 2: Development
# 開発環境用ステージ（ホットリロード有効）
# docker-compose.override.yml で target: dev を指定して使用
# -----------------------------------------------------------------------------
FROM base AS dev
# ソースコードはボリュームマウントで提供される想定だが、単体起動用にコピー
COPY src ./src
EXPOSE 8080
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.profiles=docker"]

# -----------------------------------------------------------------------------
# Stage 3: Builder
# 本番用にJARファイルをビルドするステージ
# -----------------------------------------------------------------------------
FROM base AS build
COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# -----------------------------------------------------------------------------
# Stage 4: Production
# 実行用JREのみを含む、軽量な本番環境用ステージ
# docker-compose.prod.yml で target: prod を指定して使用
# -----------------------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy AS prod
WORKDIR /app
# ビルドステージから jar ファイルをコピー
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
