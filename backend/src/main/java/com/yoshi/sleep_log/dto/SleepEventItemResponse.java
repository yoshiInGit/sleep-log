package com.yoshi.sleep_log.dto;

import com.yoshi.sleep_log.domain.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 睡眠イベント1件分のレスポンスDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SleepEventItemResponse {

    /** 睡眠イベントID */
    private Long eventId;

    /** イベント種別（SLEEP_START / SLEEP_END） */
    private EventType eventType;

    /** イベント発生時刻 */
    private LocalDateTime eventTime;

    /**
     * 睡眠の質の説明文（SLEEP_END かつ記録あり時のみ設定、それ以外は null）
     * 例: "良い" / "普通" など
     */
    private String qualityDescription;
}
