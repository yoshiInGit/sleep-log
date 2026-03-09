package com.yoshi.sleep_log.controller;

import com.yoshi.sleep_log.domain.model.SleepEvent;
import com.yoshi.sleep_log.domain.model.User;
import com.yoshi.sleep_log.domain.repository.SleepEventRepository;
import com.yoshi.sleep_log.domain.repository.UserRepository;
import com.yoshi.sleep_log.dto.SleepEndRequest;
import com.yoshi.sleep_log.dto.SleepEndResponse;
import com.yoshi.sleep_log.dto.SleepStartRequest;
import com.yoshi.sleep_log.dto.SleepStartResponse;
import com.yoshi.sleep_log.domain.value_object.SleepQuality;
import com.yoshi.sleep_log.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yoshi.sleep_log.dto.SleepEventItemResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 睡眠イベント操作コントローラー
 * 睡眠開始・終了などのイベント記録エンドポイントを提供する。
 */
@RestController
@RequestMapping("/api/sleep-events")
@RequiredArgsConstructor
public class SleepEventController {

        private final UserRepository userRepository;
        private final SleepEventRepository sleepEventRepository;

        /**
         * 睡眠開始を記録するエンドポイント。
         * startTime が省略された場合はサーバーの現在時刻を使用する。
         *
         * @param request email（必須）と startTime（任意）を含むリクエストボディ
         * @return 201 Created + 作成されたイベントの概要
         */
        @PostMapping("/start")
        public ResponseEntity<SleepStartResponse> recordSleepStart(@RequestBody SleepStartRequest request) {

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

                SleepStartResponse response = new SleepStartResponse(
                                savedEvent.getId(),
                                user.getId(),
                                savedEvent.getEventTime(),
                                "睡眠開始を記録しました");

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        /**
         * 睡眠終了を記録するエンドポイント。
         * endTime が省略された場合はサーバーの現在時刻を使用する。
         * qualityValue は 1〜5 の整数（任意）。省略した場合は睡眠の質を記録しない。
         *
         * @param request email（必須）、endTime（任意）、qualityValue（任意）を含むリクエストボディ
         * @return 201 Created + 作成されたイベントの概要
         */
        @PostMapping("/end")
        public ResponseEntity<SleepEndResponse> recordSleepEnd(@RequestBody SleepEndRequest request) {

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

                SleepEndResponse response = new SleepEndResponse(
                                savedEvent.getId(),
                                user.getId(),
                                savedEvent.getEventTime(),
                                qualityDescription,
                                "睡眠終了を記録しました");

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        /**
         * 指定期間内の睡眠イベント一覧を取得するエンドポイント。
         *
         * @param email ユーザーのメールアドレス（必須）
         * @param from  取得開始日時（必須, ISO 8601形式: yyyy-MM-ddTHH:mm:ss）
         * @param to    取得終了日時（必須, ISO 8601形式: yyyy-MM-ddTHH:mm:ss）
         * @return 200 OK + 期間内のイベントリスト（該当なしの場合は空リスト）
         */
        @GetMapping
        public ResponseEntity<List<SleepEventItemResponse>> getEventsByPeriod(
                        @RequestParam String email,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

                // メールアドレスでユーザーを取得（存在しない場合は 404 を返す）
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "指定されたメールアドレスのユーザーが見つかりません: " + email));

                // 指定期間内のイベントを取得し DTO へ変換する
                List<SleepEventItemResponse> responses = sleepEventRepository
                                .findByUserAndEventTimeBetweenOrderByEventTimeAsc(user, from, to)
                                .stream()
                                .map(e -> new SleepEventItemResponse(
                                                e.getId(),
                                                e.getEventType(),
                                                e.getEventTime(),
                                                e.getQuality() != null ? e.getQuality().getDescription() : null))
                                .collect(Collectors.toList());

                return ResponseEntity.ok(responses);
        }
}
