# SleepLog プロジェクト

最終更新日:2026/02/13

睡眠時間を可視化するサービス

## プロジェクト構成

```
SleepLogPj/
├── docker-compose.yml           # 基本設定（全環境共通）
├── docker-compose.override.yml  # 開発用設定（自動読み込み）
├── docker-compose.prod.yml      # 本番用設定
├── .env                          # 環境変数（Git管理外）
├── .env.example                  # 環境変数テンプレート
├── backend/                      # バックエンド（Spring Boot）
│   ├── Dockerfile               # 本番用
│   ├── Dockerfile.dev          # 開発用
│   ├── src/
│   └── pom.xml
└── frontend/                     # フロントエンド（予定）
```

## クイックスタート

### 初回セットアップ

```powershell
# 環境変数ファイルを作成
cp .env.example .env

# Docker環境を起動
docker-compose up
```

### アクセス

- バックエンドAPI: http://localhost:8080
- ヘルスチェック: http://localhost:8080/api/health
- データベース: localhost:5432

## 開発ガイド

### 開発環境の起動

```powershell
docker-compose up
```

ホットリロードが有効なので、コード変更は自動的に反映されます。

### 本番ビルド

```powershell
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up --build
```

### ログ確認

```powershell
# バックエンドのログ
docker-compose logs -f backend

# データベースのログ
docker-compose logs -f db
```

### コンテナの停止

```powershell
docker-compose down
```

## 技術スタック

### バックエンド

- **言語**: Java 21
- **フレームワーク**: Spring Boot 4.0.2
- **データベース**: PostgreSQL 16
- **ビルドツール**: Maven
- **コンテナ**: Docker

詳細は [backend/README.md](backend/README.md) を参照してください。

## 変更履歴

- 2026/02/13: プロジェクト初期設定、Docker環境構築
"# sleep-log" 
