# WBS-Schedule.md

最終更新日: 2026/02/16

## スケジュール (Gantt Chart)

```mermaid
gantt
    title SleepLogPj 開発スケジュール
    dateFormat  YYYY-MM-DD
    section 企画・設計
    要件定義・ドメイン設計       :done,    des1, 2026-02-13, 2026-02-16
    WBS/スケジュール作成         :active,  des2, 2026-02-16, 2026-02-17

    section バックエンド開発
    API基盤構築 (Spring Boot)   :         be1, 2026-02-18, 2026-02-21
    睡眠記録CRUD実装            :         be2, 2026-02-22, 2026-02-25

    section フロントエンド開発
    React環境構築              :         fe1, 2026-02-18, 2026-02-20
    記録画面・一覧画面作成       :         fe2, 2026-02-21, 2026-02-26

    section 統合・テスト
    フロント・バックエンド連携   :         it1, 2026-02-27, 2026-03-02
    動作確認・デプロイ準備       :         it2, 2026-03-03, 2026-03-05
```

## WBS (Work Breakdown Structure)

### 1. プロジェクト管理

- [ ] プロジェクト定義 (`Project-doc.md`)
- [ ] 管理基盤構築 (`WBS-Schedule.md`)

### 2. バックエンド (Spring Boot)

- [ ] エンティティ実装 (User, SleepLog)
- [ ] リポジトリ層実装
- [ ] APIエンドポイント実装
  - [ ] POST /api/logs (登録)
  - [ ] GET /api/logs (一覧)
  - [ ] DELETE /api/logs/{id} (削除)

### 3. フロントエンド (React)

- [ ] プロジェクト初期化
- [ ] コンポーネント作成
  - [ ] 記録入力フォーム
  - [ ] 睡眠履歴リスト
- [ ] API連携実装 (Axios)

### 4. インフラ・共通

- [ ] Docker環境の最適化
- [ ] DBマイグレーション (Flyway/Liquibase等検討)
- [ ] バリデーションルールの適用
