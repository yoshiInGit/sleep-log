package com.yoshi.sleep_log.domain.value_object;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED) // For JPA
public class SleepDuration {

    private long minutes;

    public SleepDuration(long minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException("Duration cannot be negative");
        }
        this.minutes = minutes;
    }

    public SleepDuration(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end times cannot be null");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start time cannot be after end time");
        }
        this.minutes = Duration.between(start, end).toMinutes();
    }

    public long toMinutes() {
        return this.minutes;
    }

    public long toHours() {
        return minutes / 60;
    }

    public String format() {
        long hours = toHours();
        long mins = minutes % 60;
        return String.format("%d時間%d分", hours, mins);
    }
}
