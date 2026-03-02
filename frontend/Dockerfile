# -----------------------------------------------------------------------------
# Stage 1: Base
# 共通の依存関係をインストールするステージ
# -----------------------------------------------------------------------------
FROM node:20-alpine AS base
WORKDIR /app
COPY package*.json ./
# 依存関係のインストール
RUN npm install

# -----------------------------------------------------------------------------
# Stage 2: Development
# 開発環境用ステージ（ホットリロード有効）
# docker-compose.yml で target: dev を指定して使用
# -----------------------------------------------------------------------------
FROM base AS dev
# ソースコードをコピー（注：Docker Composeのボリュームマウントで上書きされるが、単体起動用に記述）
COPY . .
EXPOSE 3000
CMD ["npm", "run", "dev", "--", "--host"]

# -----------------------------------------------------------------------------
# Stage 3: Builder
# 本番用にビルドを行うステージ
# -----------------------------------------------------------------------------
FROM base AS build
COPY . .
RUN npm run build

# -----------------------------------------------------------------------------
# Stage 4: Production
# Nginxで静的ファイルを配信する本番環境用ステージ
# docker-compose.prod.yml で target: prod を指定して使用
# -----------------------------------------------------------------------------
FROM nginx:stable-alpine AS prod
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
