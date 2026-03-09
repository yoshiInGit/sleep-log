package com.yoshi.sleep_log.usecase;

import com.yoshi.sleep_log.domain.model.SleepEvent;
import com.yoshi.sleep_log.domain.model.User;
import com.yoshi.sleep_log.domain.repository.SleepEventRepository;
import com.yoshi.sleep_log.domain.repository.UserRepository;
import com.yoshi.sleep_log.domain.value_object.SleepQuality;
import com.yoshi.sleep_log.dto.SleepEndRequest;
import com.yoshi.sleep_log.dto.SleepEndResponse;
import com.yoshi.sleep_log.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 睡眠終了記録ユースケース。
 * ユーザーを特定し、睡眠終了イベントを生成・保存する。
 */
@Service
@RequiredArgsConstructor
public class RecordSleepEndUsecase {

    private final UserRepository userRepository;
    private final SleepEventRepository sleepEventRepository;

    /**
     * 睡眠終了イベントを記録する。
     *
     * @param request email（必須）、endTime（任意）、qualityValue（任意）を含むリクエスト
     * @return 作成されたイベントの概要レスポンス
     */
    public SleepEndResponse execute(SleepEndRequest request) {

        // メールアドレスでユーザーを取得（存在しない場合は 404 を返す）
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "指定されたメールアドレスのユーザーが見つかりません: " + request.getEmail()));

        // endTime が指定されていなければサーバー現在時刻を使用する
        LocalDateTime endTime = (request.getEndTime() != null)
                ? request.getEndTime()
                : LocalDateTime.now();

        // qualityValue が指定されていれば SleepQuality へ変換する（1〜5 の範囲外は例外）
        SleepQuality quality = null;
        if (request.getQualityValue() != null) {
            quality = SleepQuality.fromValue(request.getQualityValue());
        }

        // ドメインファクトリメソッドで睡眠終了イベントを生成し保存する
        SleepEvent event = SleepEvent.end(user, endTime, quality);
        SleepEvent savedEvent = sleepEventRepository.save(event);

        String qualityDescription = (savedEvent.getQuality() != null)
                ? savedEvent.getQuality().getDescription()
                : null;

        return new SleepEndResponse(
                savedEvent.getId(),
                user.getId(),
                savedEvent.getEventTime(),
                qualityDescription,
                "睡眠終了を記録しました");
    }
}
