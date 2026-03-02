package com.yoshi.sleep_log.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * ヘルスチェック用コントローラー
 * Docker環境の動作確認に使用
 */
@RestController
@RequestMapping("/api")
public class HealthController {

    /**
     * ヘルスチェックエンドポイント
     * @return ステータス情報
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("message", "SleepLog API is running");
        return response;
    }

    /**
     * ルートエンドポイント
     * @return ウェルカムメッセージ
     */
    @GetMapping("/")
    public Map<String, String> welcome() {
        Map<String, String> response = new HashMap<>();
        response.put("application", "SleepLog API");
        response.put("version", "0.0.1-SNAPSHOT");
        response.put("description", "睡眠時間記録サービス");
        return response;
    }
}
