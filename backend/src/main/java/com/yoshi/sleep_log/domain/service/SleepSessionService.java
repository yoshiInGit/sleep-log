package com.yoshi.sleep_log.domain.service;

import com.yoshi.sleep_log.domain.model.SleepEvent;
import com.yoshi.sleep_log.domain.model.EventType;
import com.yoshi.sleep_log.domain.value_object.SleepDuration;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Comparator;

@Service
public class SleepSessionService {

    /**
     * 一連のSleepEventリストから、実際の睡眠時間を計算します。
     * START イベントの後に来る最初の END イベントまでの時間を1つのセッションとみなします。
     * 
     * @param events ユーザーの睡眠イベントリスト
     * @return 合計の睡眠時間
     */
    public SleepDuration calculateTotalDuration(List<SleepEvent> events) {
        if (events == null || events.isEmpty()) {
            return new SleepDuration(0); // 0 minutes
        }

        // 時間順にソートする (念のため)
        events.sort(Comparator.comparing(SleepEvent::getEventTime));

        long totalMinutes = 0;
        LocalDateTime lastStartTime = null;

        for (SleepEvent event : events) {
            if (event.getEventType() == EventType.SLEEP_START) {
                // 既に開始状態であれば上書き（最新の開始時刻を優先する方針など、適宜ルールを調整）
                lastStartTime = event.getEventTime();
            } else if (event.getEventType() == EventType.SLEEP_END) {
                if (lastStartTime != null) {
                    // Start -> End のペアができたため時間を加算
                    SleepDuration sessionDuration = new SleepDuration(lastStartTime, event.getEventTime());
                    totalMinutes += sessionDuration.toMinutes();

                    // 次のセッションに向けてリセット
                    lastStartTime = null;
                }
                // lastStartTime が null の場合（STARTなしのEND）は無視またはエラーハンドリング
            }
        }

        return new SleepDuration(totalMinutes);
    }
}
