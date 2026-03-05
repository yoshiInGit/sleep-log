package com.yoshi.sleep_log.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SleepStatsResponse {
    private double averageQuality;
    private double averageDurationMinutes;
    private double durationVariability; // 睡眠時間のバラつき (標準偏差)
}
