package com.yoshi.sleep_log.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 睡眠開始記録リクエストDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SleepStartRequest {

    /**
     * ユーザーのメールアドレス（ユーザー識別用）
     */
    private String email;

    /**
     * 睡眠開始時刻。null の場合はサーバー現在時刻を使用する。
     */
    private LocalDateTime startTime;
}
