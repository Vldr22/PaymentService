package org.resume.paymentservice.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.math.BigDecimal;

@Converter(autoApply = true)
public class BigDecimalToLongConverter implements AttributeConverter<BigDecimal, Long> {

    private static final int SCALE = 2;

    @Override
    public Long convertToDatabaseColumn(BigDecimal amount) {
        if (amount == null) {
            return null;
        }
        return amount.movePointRight(SCALE).longValueExact();
    }

    @Override
    public BigDecimal convertToEntityAttribute(Long dbData) {
        if (dbData == null) {
            return null;
        }
        return BigDecimal.valueOf(dbData).movePointLeft(SCALE);
    }
}
