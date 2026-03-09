package com.yoshi.sleep_log.usecase;

import com.yoshi.sleep_log.domain.model.SleepEvent;
import com.yoshi.sleep_log.domain.model.User;
import com.yoshi.sleep_log.domain.repository.SleepEventRepository;
import com.yoshi.sleep_log.domain.repository.UserRepository;
import com.yoshi.sleep_log.dto.SleepStartRequest;
import com.yoshi.sleep_log.dto.SleepStartResponse;
import com.yoshi.sleep_log.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 睡眠開始記録ユースケース。
 * ユーザーを特定し、睡眠開始イベントを生成・保存する。
 */
@Service
@RequiredArgsConstructor
public class RecordSleepStartUsecase {

    private final UserRepository userRepository;
    private final SleepEventRepository sleepEventRepository;

    /**
     * 睡眠開始イベントを記録する。
     *
     * @param request email（必須）と startTime（任意）を含むリクエスト
     * @return 作成されたイベントの概要レスポンス
     */
    public SleepStartResponse execute(SleepStartRequest request) {

        // メールアドレスでユーザーを取得（存在しない場合は 404 を返す）
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "指定されたメールアドレスのユーザーが見つかりません: " + request.getEmail()));

        // startTime が指定されていなければサーバー現在時刻を使用する
        LocalDateTime startTime = (request.getStartTime() != null)
                ? request.getStartTime()
                : LocalDateTime.now();

        // ドメインファクトリメソッドで睡眠開始イベントを生成し保存する
        SleepEvent event = SleepEvent.start(user, startTime);
        SleepEvent savedEvent = sleepEventRepository.save(event);

        return new SleepStartResponse(
                savedEvent.getId(),
                user.getId(),
                savedEvent.getEventTime(),
                "睡眠開始を記録しました");
    }
}
