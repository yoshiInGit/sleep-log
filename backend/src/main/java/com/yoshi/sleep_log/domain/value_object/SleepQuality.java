package com.yoshi.sleep_log.domain.value_object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum SleepQuality {
    VERY_BAD(1, "悪い"),
    BAD(2, "やや悪い"),
    NORMAL(3, "普通"),
    GOOD(4, "良い"),
    VERY_GOOD(5, "とても良い");

    private final int value;
    private final String description;

    // 数値からEnumを逆引きするためのキャッシュ
    private static final Map<Integer, SleepQuality> VALUE_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(SleepQuality::getValue, Function.identity()));

    public static SleepQuality fromValue(int value) {
        SleepQuality result = VALUE_MAP.get(value);
        if (result == null) {
            throw new IllegalArgumentException("Invalid sleep quality value: " + value);
        }
        return result;
    }
}
