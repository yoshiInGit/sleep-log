package com.yoshi.sleep_log.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 睡眠終了記録レスポンスDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SleepEndResponse {

    /** 作成された睡眠イベントのID */
    private Long eventId;

    /** 対象ユーザーのID */
    private Long userId;

    /** 記録された睡眠終了時刻 */
    private LocalDateTime endTime;

    /** 記録された睡眠の質（null の場合は未設定） */
    private String qualityDescription;

    /** 処理結果メッセージ */
    private String message;
}
