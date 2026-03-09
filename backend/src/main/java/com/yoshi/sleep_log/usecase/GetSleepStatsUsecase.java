package com.yoshi.sleep_log.usecase;

import com.yoshi.sleep_log.domain.model.EventType;
import com.yoshi.sleep_log.domain.model.SleepEvent;
import com.yoshi.sleep_log.domain.model.User;
import com.yoshi.sleep_log.domain.repository.SleepEventRepository;
import com.yoshi.sleep_log.domain.repository.UserRepository;
import com.yoshi.sleep_log.domain.service.SleepSessionService;
import com.yoshi.sleep_log.domain.value_object.SleepDuration;
import com.yoshi.sleep_log.dto.SleepStatsResponse;
import com.yoshi.sleep_log.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 睡眠統計取得ユースケース。
 * 睡眠の質（平均）、平均睡眠時間、睡眠時間のバラつき（標準偏差）を算出して返す。
 */
@Service
@RequiredArgsConstructor
public class GetSleepStatsUsecase {

    private final UserRepository userRepository;
    private final SleepEventRepository sleepEventRepository;
    private final SleepSessionService sleepSessionService;

    /**
     * 指定ユーザーの最近の睡眠統計を取得する。
     *
     * @param email ユーザーのメールアドレス
     * @return 睡眠統計レスポンス
     */
    public SleepStatsResponse execute(String email) {

        // メールアドレスでユーザーを取得（存在しない場合は 404 を返す）
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "指定されたメールアドレスのユーザーが見つかりません: " + email));

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

        // 平均睡眠時間（分）を算出
        double averageDuration = minutesList.stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);

        // 睡眠時間のバラつき（標準偏差）を算出
        double variability = calculateStandardDeviation(minutesList, averageDuration);

        return new SleepStatsResponse(averageQuality, averageDuration, variability);
    }

    /**
     * 標準偏差を算出する。
     *
     * @param values 計算対象の値リスト（分単位）
     * @param mean   平均値
     * @return 標準偏差（値が空の場合は 0.0）
     */
    private double calculateStandardDeviation(List<Long> values, double mean) {
        if (values.isEmpty())
            return 0.0;
        double sumOfSquares = values.stream()
                .mapToDouble(v -> Math.pow(v - mean, 2))
                .sum();
        return Math.sqrt(sumOfSquares / values.size());
    }
}
