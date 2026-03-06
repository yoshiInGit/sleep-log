package com.yoshi.sleep_log.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 睡眠開始記録レスポンスDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SleepStartResponse {

    /** 作成された睡眠イベントのID */
    private Long eventId;

    /** 対象ユーザーのID */
    private Long userId;

    /** 記録された睡眠開始時刻 */
    private LocalDateTime startTime;

    /** 処理結果メッセージ */
    private String message;
}
