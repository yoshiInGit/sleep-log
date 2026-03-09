package com.yoshi.sleep_log.usecase;

import com.yoshi.sleep_log.domain.model.User;
import com.yoshi.sleep_log.domain.repository.SleepEventRepository;
import com.yoshi.sleep_log.domain.repository.UserRepository;
import com.yoshi.sleep_log.dto.SleepEventItemResponse;
import com.yoshi.sleep_log.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 睡眠イベント一覧取得ユースケース。
 * 指定された期間内の睡眠イベントをユーザー別に取得して返す。
 */
@Service
@RequiredArgsConstructor
public class GetSleepEventsUsecase {

    private final UserRepository userRepository;
    private final SleepEventRepository sleepEventRepository;

    /**
     * 指定期間内の睡眠イベント一覧を取得する。
     *
     * @param email ユーザーのメールアドレス
     * @param from  取得開始日時
     * @param to    取得終了日時
     * @return 期間内のイベントレスポンスリスト（該当なしの場合は空リスト）
     */
    public List<SleepEventItemResponse> execute(String email, LocalDateTime from, LocalDateTime to) {

        // メールアドレスでユーザーを取得（存在しない場合は 404 を返す）
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "指定されたメールアドレスのユーザーが見つかりません: " + email));

        // 指定期間内のイベントを取得し DTO へ変換する
        return sleepEventRepository
                .findByUserAndEventTimeBetweenOrderByEventTimeAsc(user, from, to)
                .stream()
                .map(e -> new SleepEventItemResponse(
                        e.getId(),
                        e.getEventType(),
                        e.getEventTime(),
                        e.getQuality() != null ? e.getQuality().getDescription() : null))
                .collect(Collectors.toList());
    }
}
