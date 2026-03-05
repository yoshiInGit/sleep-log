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
        return new SleepDuration(calculateSessionDurations(events).stream()
                .mapToLong(SleepDuration::toMinutes)
                .sum());
    }

    /**
     * イベントリストから個々の睡眠セッションの時間を取得します。
     */
    public List<SleepDuration> calculateSessionDurations(List<SleepEvent> events) {
        if (events == null || events.isEmpty()) {
            return List.of();
        }

        // 時間順にソートする
        events.sort(Comparator.comparing(SleepEvent::getEventTime));

        java.util.List<SleepDuration> durations = new java.util.ArrayList<>();
        LocalDateTime lastStartTime = null;

        for (SleepEvent event : events) {
            if (event.getEventType() == EventType.SLEEP_START) {
                lastStartTime = event.getEventTime();
            } else if (event.getEventType() == EventType.SLEEP_END) {
                if (lastStartTime != null) {
                    durations.add(new SleepDuration(lastStartTime, event.getEventTime()));
                    lastStartTime = null;
                }
            }
        }
        return durations;
    }
}
