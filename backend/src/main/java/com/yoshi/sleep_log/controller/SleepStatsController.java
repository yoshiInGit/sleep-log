package com.yoshi.sleep_log.controller;

import com.yoshi.sleep_log.domain.model.EventType;
import com.yoshi.sleep_log.domain.model.SleepEvent;
import com.yoshi.sleep_log.domain.model.User;
import com.yoshi.sleep_log.domain.repository.SleepEventRepository;
import com.yoshi.sleep_log.domain.repository.UserRepository;
import com.yoshi.sleep_log.domain.service.SleepSessionService;
import com.yoshi.sleep_log.domain.value_object.SleepDuration;
import com.yoshi.sleep_log.dto.SleepStatsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sleep-stats")
@RequiredArgsConstructor
public class SleepStatsController {

    private final SleepEventRepository sleepEventRepository;
    private final UserRepository userRepository;
    private final SleepSessionService sleepSessionService;

    /**
     * 最近の睡眠統計を取得します。
     * 睡眠の質（平均）、平均睡眠時間、睡眠時間のバラつき（標準偏差）を返します。
     */
    @GetMapping("/recent")
    public ResponseEntity<SleepStatsResponse> getRecentStats(@RequestParam String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<SleepEvent> events = sleepEventRepository.findByUserOrderByEventTimeAsc(user);

        // 睡眠の質の平均を算出
        double averageQuality = events.stream()
                .filter(e -> e.getEventType() == EventType.SLEEP_END && e.getQuality() != null)
                .mapToInt(e -> e.getQuality().getValue())
                .average()
                .orElse(0.0);

        // 個々のセッション時間を取得
        List<SleepDuration> sessions = sleepSessionService.calculateSessionDurations(events);
        List<Long> minutesList = sessions.stream()
                .map(SleepDuration::toMinutes)
                .collect(Collectors.toList());

        // 平均睡眠時間（分）
        double averageDuration = minutesList.stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);

        // 睡眠時間のバラつき（標準偏差）を算出
        double variability = calculateStandardDeviation(minutesList, averageDuration);

        return ResponseEntity.ok(new SleepStatsResponse(averageQuality, averageDuration, variability));
    }

    private double calculateStandardDeviation(List<Long> values, double mean) {
        if (values.isEmpty())
            return 0.0;
        double sumOfSquares = values.stream()
                .mapToDouble(v -> Math.pow(v - mean, 2))
                .sum();
        return Math.sqrt(sumOfSquares / values.size());
    }
}
