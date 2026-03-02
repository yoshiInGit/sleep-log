package com.yoshi.sleep_log.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.yoshi.sleep_log.domain.value_object.SleepQuality;
import com.yoshi.sleep_log.domain.value_object.SleepDuration;
import com.yoshi.sleep_log.domain.value_object.SleepQualityConverter;

@Entity
@Table(name = "sleep_logs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SleepLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "sleep_start_time")
    private LocalDateTime sleepStartTime;

    @Column(name = "sleep_end_time")
    private LocalDateTime sleepEndTime;

    // Value Objectとして変換して使用（DBにはIntegerで保存）
    @Column(name = "sleep_quality")
    @Convert(converter = SleepQualityConverter.class) // Converterが必要
    private SleepQuality sleepQuality;

    public SleepLog(User user, LocalDateTime sleepStartTime, LocalDateTime sleepEndTime, SleepQuality sleepQuality) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (sleepStartTime == null || sleepEndTime == null) {
            throw new IllegalArgumentException("Sleep times cannot be null");
        }
        if (sleepStartTime.isAfter(sleepEndTime)) {
            throw new IllegalArgumentException("Start time cannot be after end time");
        }
        this.user = user;
        this.sleepStartTime = sleepStartTime;
        this.sleepEndTime = sleepEndTime;
        this.sleepQuality = sleepQuality;

    }

    public void updateQuality(SleepQuality sleepQuality) {
        if (sleepQuality == null) {
            throw new IllegalArgumentException("Sleep quality cannot be null");
        }
        this.sleepQuality = sleepQuality;

    }

    public void updateSleepTimes(LocalDateTime sleepStartTime, LocalDateTime sleepEndTime) {
        if (sleepStartTime == null || sleepEndTime == null || sleepStartTime.isAfter(sleepEndTime)) {
            throw new IllegalArgumentException("Invalid sleep times");
        }
        this.sleepStartTime = sleepStartTime;
        this.sleepEndTime = sleepEndTime;

    }

    public SleepDuration getDuration() {
        return new SleepDuration(sleepStartTime, sleepEndTime);
    }

    public boolean isHealthy() {
        SleepDuration duration = getDuration();
        return duration.toHours() >= 6 && duration.toHours() <= 9 && sleepQuality.getValue() >= 3;
    }

    // sleepStartTimeが午前４時までなら、前日判定、それ以降は当日寝たとして判定する
    public LocalDate getSleepDate() {
        if (sleepStartTime.getHour() < 4) {
            return sleepStartTime.toLocalDate().minusDays(1);
        }
        return sleepStartTime.toLocalDate();
    }

}