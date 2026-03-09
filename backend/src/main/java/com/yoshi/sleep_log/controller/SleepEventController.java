package com.yoshi.sleep_log.controller;

import com.yoshi.sleep_log.dto.SleepEndRequest;
import com.yoshi.sleep_log.dto.SleepEndResponse;
import com.yoshi.sleep_log.dto.SleepEventItemResponse;
import com.yoshi.sleep_log.dto.SleepStartRequest;
import com.yoshi.sleep_log.dto.SleepStartResponse;
import com.yoshi.sleep_log.usecase.GetSleepEventsUsecase;
import com.yoshi.sleep_log.usecase.RecordSleepEndUsecase;
import com.yoshi.sleep_log.usecase.RecordSleepStartUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 睡眠イベント操作コントローラー。
 * リクエストの受け取りとレスポンスの返却に専念し、
 * ビジネスロジックは各 Usecase クラスへ委譲する。
 */
@RestController
@RequestMapping("/api/sleep-events")
@RequiredArgsConstructor
public class SleepEventController {

        private final RecordSleepStartUsecase recordSleepStartUsecase;
        private final RecordSleepEndUsecase recordSleepEndUsecase;
        private final GetSleepEventsUsecase getSleepEventsUsecase;

        /**
         * 睡眠開始を記録するエンドポイント。
         *
         * @param request email（必須）と startTime（任意）を含むリクエストボディ
         * @return 201 Created + 作成されたイベントの概要
         */
        @PostMapping("/start")
        public ResponseEntity<SleepStartResponse> recordSleepStart(@RequestBody SleepStartRequest request) {
                SleepStartResponse response = recordSleepStartUsecase.execute(request);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        /**
         * 睡眠終了を記録するエンドポイント。
         *
         * @param request email（必須）、endTime（任意）、qualityValue（任意）を含むリクエストボディ
         * @return 201 Created + 作成されたイベントの概要
         */
        @PostMapping("/end")
        public ResponseEntity<SleepEndResponse> recordSleepEnd(@RequestBody SleepEndRequest request) {
                SleepEndResponse response = recordSleepEndUsecase.execute(request);
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
                List<SleepEventItemResponse> response = getSleepEventsUsecase.execute(email, from, to);
                return ResponseEntity.ok(response);
        }
}
