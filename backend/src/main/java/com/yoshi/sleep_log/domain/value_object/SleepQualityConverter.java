package com.yoshi.sleep_log.domain.value_object;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SleepQualityConverter implements AttributeConverter<SleepQuality, Integer> {

    @Override
    public Integer convertToDatabaseColumn(SleepQuality attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public SleepQuality convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return SleepQuality.fromValue(dbData);
    }
}
