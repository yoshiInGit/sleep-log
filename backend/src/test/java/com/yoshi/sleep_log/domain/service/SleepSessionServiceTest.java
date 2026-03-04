package com.yoshi.sleep_log.domain.service;

import com.yoshi.sleep_log.domain.model.EventType;
import com.yoshi.sleep_log.domain.model.SleepEvent;
import com.yoshi.sleep_log.domain.model.User;
import com.yoshi.sleep_log.domain.value_object.SleepDuration;
import com.yoshi.sleep_log.domain.value_object.SleepQuality;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SleepSessionServiceTest {

    private SleepSessionService sleepSessionService;
    private User testUser;

    @BeforeEach
    void setUp() {
        sleepSessionService = new SleepSessionService();
        testUser = new User("Test User", "test@example.com", "password");
    }

    @Test
    void calculateTotalDuration_withSingleSession() {
        // Arrange
        LocalDateTime start = LocalDateTime.of(2023, 10, 1, 22, 0); // 22:00
        LocalDateTime end = LocalDateTime.of(2023, 10, 2, 6, 0); // 06:00
        List<SleepEvent> events = Arrays.asList(
                new SleepEvent(testUser, start, EventType.SLEEP_START, null),
                new SleepEvent(testUser, end, EventType.SLEEP_END, SleepQuality.GOOD));

        // Act
        SleepDuration totalDuration = sleepSessionService.calculateTotalDuration(events);

        // Assert
        assertEquals(8 * 60, totalDuration.toMinutes()); // 8 hours
    }

    @Test
    void calculateTotalDuration_withMultipleSessions() {
        // Arrange
        LocalDateTime start1 = LocalDateTime.of(2023, 10, 1, 23, 0); // 23:00
        LocalDateTime end1 = LocalDateTime.of(2023, 10, 2, 3, 0); // 03:00 (4 hours)

        LocalDateTime start2 = LocalDateTime.of(2023, 10, 2, 4, 0); // 04:00
        LocalDateTime end2 = LocalDateTime.of(2023, 10, 2, 8, 30); // 08:30 (4.5 hours)

        List<SleepEvent> events = Arrays.asList(
                new SleepEvent(testUser, start1, EventType.SLEEP_START, null),
                new SleepEvent(testUser, end1, EventType.SLEEP_END, SleepQuality.NORMAL),
                new SleepEvent(testUser, start2, EventType.SLEEP_START, null),
                new SleepEvent(testUser, end2, EventType.SLEEP_END, SleepQuality.GOOD));

        // Act
        SleepDuration totalDuration = sleepSessionService.calculateTotalDuration(events);

        // Assert
        assertEquals(8 * 60 + 30, totalDuration.toMinutes()); // 8.5 hours
    }

    @Test
    void calculateTotalDuration_withNoEvents() {
        // Act
        SleepDuration totalDuration = sleepSessionService.calculateTotalDuration(Collections.emptyList());

        // Assert
        assertEquals(0, totalDuration.toMinutes());
    }

    @Test
    void calculateTotalDuration_withOnlyStartEvent() {
        // Arrange
        LocalDateTime start = LocalDateTime.of(2023, 10, 1, 22, 0); // 22:00
        List<SleepEvent> events = Collections.singletonList(
                new SleepEvent(testUser, start, EventType.SLEEP_START, null));

        // Act
        SleepDuration totalDuration = sleepSessionService.calculateTotalDuration(events);

        // Assert
        // The session is incomplete, so duration should be 0 based on our logic
        assertEquals(0, totalDuration.toMinutes());
    }

    @Test
    void calculateTotalDuration_withUnorderedEvents() {
        // Arrange
        LocalDateTime start = LocalDateTime.of(2023, 10, 1, 22, 0); // 22:00
        LocalDateTime end = LocalDateTime.of(2023, 10, 2, 6, 0); // 06:00

        // Pass end event before start event
        List<SleepEvent> events = Arrays.asList(
                new SleepEvent(testUser, end, EventType.SLEEP_END, SleepQuality.GOOD),
                new SleepEvent(testUser, start, EventType.SLEEP_START, null));

        // Act
        SleepDuration totalDuration = sleepSessionService.calculateTotalDuration(events);

        // Assert
        assertEquals(8 * 60, totalDuration.toMinutes()); // Should sort them internally
    }
}
