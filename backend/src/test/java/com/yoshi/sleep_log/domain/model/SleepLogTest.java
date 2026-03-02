package com.yoshi.sleep_log.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.yoshi.sleep_log.domain.value_object.SleepQuality;

class SleepLogTest {

    private final LocalDateTime now = LocalDateTime.of(2026, 2, 18, 7, 0);
    private final LocalDateTime nightBefore = now.minusHours(8); // 8時間前
    private final User testUser = new User("テストユーザー", "test@example.com", "hash");

    @Nested
    @DisplayName("コンストラクタのテスト")
    class ConstructorTest {

        @Test
        @DisplayName("有効な引数でインスタンスが生成されること")
        void constructor_WithValidArgs_ShouldCreateInstance() {
            // Act
            SleepLog sleepLog = new SleepLog(testUser, nightBefore, now, SleepQuality.GOOD);

            // Assert
            assertEquals(nightBefore, sleepLog.getSleepStartTime());
            assertEquals(now, sleepLog.getSleepEndTime());
            assertEquals(SleepQuality.GOOD, sleepLog.getSleepQuality());
        }

        @Test
        @DisplayName("ユーザーがnullの場合に例外がスローされること")
        void constructor_WithNullUser_ShouldThrowException() {
            assertThrows(IllegalArgumentException.class,
                    () -> new SleepLog(null, nightBefore, now, SleepQuality.NORMAL));
        }

        @Test
        @DisplayName("開始時刻がnullの場合に例外がスローされること")
        void constructor_WithNullStartTime_ShouldThrowException() {
            assertThrows(IllegalArgumentException.class, () -> new SleepLog(testUser, null, now, SleepQuality.NORMAL));
        }

        @Test
        @DisplayName("開始時刻が終了時刻より後の場合に例外がスローされること")
        void constructor_WithStartTimeAfterEndTime_ShouldThrowException() {
            LocalDateTime invalidStart = now.plusHours(1);
            assertThrows(IllegalArgumentException.class,
                    () -> new SleepLog(testUser, invalidStart, now, SleepQuality.NORMAL));
        }
    }

    @Nested
    @DisplayName("更新メソッドのテスト")
    class UpdateTest {

        @Test
        @DisplayName("睡眠の質を更新できること")
        void updateQuality_ShouldUpdateValue() {
            // Arrange
            SleepLog sleepLog = new SleepLog(testUser, nightBefore, now, SleepQuality.NORMAL);

            // Act
            sleepLog.updateQuality(SleepQuality.VERY_GOOD);

            // Assert
            assertEquals(SleepQuality.VERY_GOOD, sleepLog.getSleepQuality());
        }

        @Test
        @DisplayName("睡眠時刻を更新できること")
        void updateSleepTimes_ShouldUpdateValues() {
            // Arrange
            SleepLog sleepLog = new SleepLog(testUser, nightBefore, now, SleepQuality.NORMAL);
            LocalDateTime newStart = nightBefore.minusHours(1);
            LocalDateTime newEnd = now.plusHours(1);

            // Act
            sleepLog.updateSleepTimes(newStart, newEnd);

            // Assert
            assertEquals(newStart, sleepLog.getSleepStartTime());
            assertEquals(newEnd, sleepLog.getSleepEndTime());
        }
    }

    @Nested
    @DisplayName("isHealthyのテスト (健康判定)")
    class HealthTest {

        @Test
        @DisplayName("健康な睡眠条件を満たす場合にtrueを返すこと (8時間、質:GOOD)")
        void isHealthy_WithHealthyConditions_ShouldReturnTrue() {
            // 8時間眠り、質が良い状態
            SleepLog sleepLog = new SleepLog(testUser, nightBefore, now, SleepQuality.GOOD);
            assertTrue(sleepLog.isHealthy());
        }

        @Test
        @DisplayName("睡眠時間が短すぎる場合にfalseを返すこと (5時間)")
        void isHealthy_WithShortDuration_ShouldReturnFalse() {
            LocalDateTime shortStart = now.minusHours(5);
            SleepLog sleepLog = new SleepLog(testUser, shortStart, now, SleepQuality.GOOD);
            assertFalse(sleepLog.isHealthy());
        }

        @Test
        @DisplayName("睡眠時間が長すぎる場合にfalseを返すこと (10時間)")
        void isHealthy_WithLongDuration_ShouldReturnFalse() {
            LocalDateTime longStart = now.minusHours(10);
            SleepLog sleepLog = new SleepLog(testUser, longStart, now, SleepQuality.GOOD);
            assertFalse(sleepLog.isHealthy());
        }

        @Test
        @DisplayName("睡眠の質が低い場合にfalseを返すこと (8時間、質:BAD)")
        void isHealthy_WithLowQuality_ShouldReturnFalse() {
            SleepLog sleepLog = new SleepLog(testUser, nightBefore, now, SleepQuality.BAD);
            assertFalse(sleepLog.isHealthy());
        }
    }

    @Nested
    @DisplayName("getSleepDateのテスト (睡眠日計上判定)")
    class SleepDateTest {

        @Test
        @DisplayName("午前0時に寝た場合、前日の日付を返すこと")
        void getSleepDate_AtMidnight_ShouldReturnPreviousDate() {
            LocalDateTime midnight = LocalDateTime.of(2026, 2, 20, 0, 0);
            SleepLog sleepLog = new SleepLog(testUser, midnight, midnight.plusHours(7), SleepQuality.GOOD);
            assertEquals(LocalDate.of(2026, 2, 19), sleepLog.getSleepDate());
        }

        @Test
        @DisplayName("午前3時59分に寝た場合、前日の日付を返すこと")
        void getSleepDate_AtBefore4AM_ShouldReturnPreviousDate() {
            LocalDateTime three59 = LocalDateTime.of(2026, 2, 20, 3, 59);
            SleepLog sleepLog = new SleepLog(testUser, three59, three59.plusHours(7), SleepQuality.GOOD);
            assertEquals(LocalDate.of(2026, 2, 19), sleepLog.getSleepDate());
        }

        @Test
        @DisplayName("午前4時ちょうどに寝た場合、当日の日付を返すこと")
        void getSleepDate_At4AM_ShouldReturnCurrentDate() {
            LocalDateTime fourAM = LocalDateTime.of(2026, 2, 20, 4, 0);
            SleepLog sleepLog = new SleepLog(testUser, fourAM, fourAM.plusHours(7), SleepQuality.GOOD);
            assertEquals(LocalDate.of(2026, 2, 20), sleepLog.getSleepDate());
        }

        @Test
        @DisplayName("昼の12時に寝た場合、当日の日付を返すこと")
        void getSleepDate_AtNoon_ShouldReturnCurrentDate() {
            LocalDateTime noon = LocalDateTime.of(2026, 2, 20, 12, 0);
            SleepLog sleepLog = new SleepLog(testUser, noon, noon.plusHours(7), SleepQuality.GOOD);
            assertEquals(LocalDate.of(2026, 2, 20), sleepLog.getSleepDate());
        }
    }
}
