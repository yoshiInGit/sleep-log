# SleepLog バックエンド - Docker 開発環境

> **注意**: Docker Compose ファイルはプロジェクトのルートディレクトリに配置されています。
> このディレクトリではなく、プロジェクトルート (`../`) でコマンドを実行してください。

## クイックスタート

### 開発環境の起動

```powershell
# プロジェクトルートに移動
cd ..

# Docker環境を起動
docker-compose up
```

## ファイル構成

```
backend/
├── Dockerfile                      # 本番用（マルチステージビルド、最適化済み）
├── Dockerfile.dev                  # 開発用（ホットリロード対応）
├── docker-compose.yml              # 基本設定（全環境共通）
├── docker-compose.override.yml    # 開発用設定（自動読み込み）
└── docker-compose.prod.yml        # 本番用設定
```

## Docker Compose ファイルの役割

### docker-compose.yml

- データベース設定
- アプリケーションの共通設定
- ネットワーク・ボリューム定義

### docker-compose.override.yml（開発専用）

- `Dockerfile.dev` を使用
- ソースコードのボリュームマウント
- Maven キャッシュの設定
- **`docker-compose up` で自動的に読み込まれる**

### docker-compose.prod.yml（本番専用）

- `Dockerfile` を使用（最適化された jar 実行）
- 明示的に `-f` オプションで指定が必要

## 便利なコマンド

### 開発時

```powershell
# 起動（ホットリロード有効）
docker-compose up

# バックグラウンド起動
docker-compose up -d

# ログ確認
docker-compose logs -f app

# 再ビルドして起動
docker-compose up --build

# 停止
docker-compose down
```

### 本番用イメージのビルド・テスト

```powershell
# 本番用イメージをビルド
docker-compose -f docker-compose.yml -f docker-compose.prod.yml build

# 本番用イメージで起動
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up

# クリーンアップ
docker-compose -f docker-compose.yml -f docker-compose.prod.yml down
```

### データベース接続

```powershell
# PostgreSQL に接続
docker-compose exec db psql -U sleeplog -d sleeplog

# テーブル一覧
\dt

# 終了
\q
```

## 環境設定

### 環境変数（.env ファイル）

環境変数は `.env` ファイルで管理されます：

```env
# データベース設定
POSTGRES_DB=sleeplog
POSTGRES_USER=sleeplog
POSTGRES_PASSWORD=sleeplog123

# アプリケーション設定
SPRING_PROFILES_ACTIVE=docker
```

> [!IMPORTANT]
> `.env` ファイルは Git にコミットされません。初回セットアップ時に `.env.example` からコピーして作成してください。

### データベース

- **ホスト**: localhost (ホストから) / db (コンテナ間)
- **ポート**: 5432
- **データベース名**: sleeplog
- **ユーザー名**: sleeplog
- **パスワード**: sleeplog123

### アプリケーション

- **ポート**: 8080
- **プロファイル**: docker
- **設定ファイル**: `src/main/resources/application-docker.yml`

## ホットリロードについて

開発環境（`docker-compose up`）では以下の変更が自動的に反映されます：

- Java ファイルの変更
- リソースファイルの変更

Spring Boot DevTools が変更を検知し、自動的に再起動します。

## トラブルシューティング

### Docker Desktop が起動していない

```
error during connect: ... dockerDesktopLinuxEngine
```

→ Docker Desktop を起動してください

### ポートがすでに使用されている

```
port is already allocated
```

→ 既存のコンテナを停止: `docker-compose down`

### イメージを完全にリビルドしたい

```powershell
docker-compose down
docker-compose build --no-cache
docker-compose up
```

## Git 管理

### 環境変数ファイル

- `.env` - **Git にコミットしない**（.gitignore に含まれる）
  - 個人の開発環境や本番環境の実際の認証情報を含む
- `.env.example` - **Git にコミットする**
  - テンプレートとしての役割
  - パスワードなどはプレースホルダー値を使用

### docker-compose.override.yml

開発環境設定は共有するため、`.gitignore` に追加していません。個人用の設定が必要な場合は `.gitignore` に追加してください。
