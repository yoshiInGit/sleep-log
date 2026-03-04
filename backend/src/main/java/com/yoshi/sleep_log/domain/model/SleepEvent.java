package com.yoshi.sleep_log.domain.model;

import com.yoshi.sleep_log.domain.value_object.SleepQuality;
import com.yoshi.sleep_log.domain.value_object.SleepQualityConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "sleep_events")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SleepEvent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "event_time", nullable = false)
    private LocalDateTime eventTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    // 起床イベント時にのみ設定される値を想定
    @Column(name = "sleep_quality")
    @Convert(converter = SleepQualityConverter.class)
    private SleepQuality quality;

    public SleepEvent(User user, LocalDateTime eventTime, EventType eventType, SleepQuality quality) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (eventTime == null) {
            throw new IllegalArgumentException("Event time cannot be null");
        }
        if (eventType == null) {
            throw new IllegalArgumentException("Event type cannot be null");
        }
        // 就寝イベントの時はQualityはなし、といったドメインルールを後で追加可能
        if (eventType == EventType.SLEEP_START && quality != null) {
            throw new IllegalArgumentException("Cannot set quality on SLEEP_START event");
        }

        this.user = user;
        this.eventTime = eventTime;
        this.eventType = eventType;
        this.quality = quality;
    }

    public static SleepEvent start(User user, LocalDateTime time) {
        return new SleepEvent(user, time, EventType.SLEEP_START, null);
    }

    public static SleepEvent end(User user, LocalDateTime time, SleepQuality quality) {
        return new SleepEvent(user, time, EventType.SLEEP_END, quality);
    }
}
