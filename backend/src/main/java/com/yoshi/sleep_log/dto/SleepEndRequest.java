package com.yoshi.sleep_log.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 睡眠終了記録リクエストDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SleepEndRequest {

    /**
     * ユーザーのメールアドレス（ユーザー識別用）
     */
    private String email;

    /**
     * 睡眠終了時刻。null の場合はサーバー現在時刻を使用する。
     */
    private LocalDateTime endTime;

    /**
     * 睡眠の質スコア（1〜5）。null の場合は設定しない。
     * 1: 悪い / 2: やや悪い / 3: 普通 / 4: 良い / 5: とても良い
     */
    private Integer qualityValue;
}
