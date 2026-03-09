package com.yoshi.sleep_log.controller;

import com.yoshi.sleep_log.dto.SleepStatsResponse;
import com.yoshi.sleep_log.usecase.GetSleepStatsUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 睡眠統計コントローラー。
 * リクエストの受け取りとレスポンスの返却に専念し、
 * ビジネスロジックは GetSleepStatsUsecase へ委譲する。
 */
@RestController
@RequestMapping("/api/sleep-stats")
@RequiredArgsConstructor
public class SleepStatsController {

        private final GetSleepStatsUsecase getSleepStatsUsecase;

        /**
         * 最近の睡眠統計を取得するエンドポイント。
         * 睡眠の質（平均）、平均睡眠時間、睡眠時間のバラつき（標準偏差）を返す。
         *
         * @param email ユーザーのメールアドレス
         * @return 睡眠統計レスポンス
         */
        @GetMapping("/recent")
        public ResponseEntity<SleepStatsResponse> getRecentStats(@RequestParam String email) {
                SleepStatsResponse response = getSleepStatsUsecase.execute(email);
                return ResponseEntity.ok(response);
        }
}
