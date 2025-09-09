package co.kr.toybackend.config.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class BooleanConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean value) {
        return value != null ? (value ? "Y" : "N") : null;
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return dbData != null ? dbData.equals("Y") : null;
    }
}
