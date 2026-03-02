# 実装・学習ガイド (Implementation-Guide.md)

このドキュメントでは、`SleepLogPj` をどのように実装していくか、その流れと重要なコンセプトを解説します。学習を目的としたステップバイステップのガイドです。

## 1. 開発の全体像

本プロジェクトでは、**ドメイン駆動設計 (DDD)** の考え方を一部取り入れ、ビジネスロジックを中心に据えた開発を行います。

### 開発順序（推奨）

1.  **バックエンド: ドメイン層の実装** (Entities, Value Objects)
2.  **バックエンド: インフラ層の実装** (Repositories, DB連携)
3.  **バックエンド: アプリケーション層の実装** (Services, API Controllers)
4.  **フロントエンド: 基盤構築** (React, APIクライアント)
5.  **フロントエンド: UI実装** (Components, State管理)

---

## 2. バックエンド実装の詳細 (Spring Boot)

### ステップ1: ドメインエンティティの作成

[Domain-design.md](file:///c:/Users/yoshi/OneDrive/デスクトップ/projects/SleepLogPj/Domain-design.md) で定義したモデルをコードに落とし込みます。

- **Entity**: 一意のIDを持ち、状態が変化するもの（例: `SleepLog`）。
- **Value Object**: 属性の組み合わせ自体が意味を持ち、不変であるもの（例: `SleepQuality`）。

> **学習ポイント**: なぜIDが必要なのか、なぜ「不変（Immutable）」にするのかを意識してみましょう。

### ステップ2: リポジトリの実装

Spring Data JPAを使用して、データベースとのやり取りを定義します。

```java
// 例
public interface SleepLogRepository extends JpaRepository<SleepLog, Long> {
    List<SleepLog> findByUserId(Long userId);
}
```

### ステップ3: ドメインサービスとコントローラー

- **Service**: 複数のエンティティを跨ぐロジックや、複雑な計算（平均睡眠時間の算出など）を記述します。
- **Controller**: HTTPリクエストを受け取り、適切なServiceを呼び出し、レスポンスを返します。

---

## 3. フロントエンド実装の詳細 (React)

### ステップ1: コンポーネントの分割

UIを「入力フォーム」「グラフ」「履歴リスト」などの小さな単位（コンポーネント）に分けます。

### ステップ2: 状態管理 (State Management)

ユーザーが入力したデータや、APIから取得したデータをどこで管理するかを決めます（useState, useContextなど）。

### ステップ3: API連携 (Axios)

バックエンドのAPIを呼び出します。非同期処理（Async/Await）の理解が重要です。

---

## 4. 学習のためのTips

### 1. 「なぜ？」を大切にする

「なぜこのライブラリを使うのか？」「なぜこの構成にするのか？」を常に考えることで、技術の表面だけでなく本質を理解できます。

### 2. インクリメンタルな開発

いきなり全てを作ろうとせず、「まずは睡眠ログを1件保存して表示するだけ」といった最小の機能を完成させることから始めてください。

### 3. テストを書く

JUnitなどを使ってテストコードを書くことで、自分の書いたコードが正しく動く確信が得られ、開発効率が上がります。

---

## 次に行うべき具体的な作業

1.  `backend/src/main/java/com/yoshi/sleeplog/domain/model` ディレクトリを作成する。
2.  `SleepLog` エンティティを作成し、バリデーションルール（就寝 < 起床）を実装する。
