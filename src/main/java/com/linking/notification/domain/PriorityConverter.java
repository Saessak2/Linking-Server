package com.linking.notification.domain;

import javax.persistence.AttributeConverter;

public class PriorityConverter implements AttributeConverter<String, Integer> {

    @Override
    public Integer convertToDatabaseColumn(String attribute) {
        if ("ALL".equals(attribute))
            return 1;
        else if ("NO_MAIL".equals(attribute))
            return 2;
        return null;
    }

    @Override
    public String convertToEntityAttribute(Integer dbData) {
        if (1 == dbData)
            return "ALL";
        else if (2 == dbData)
            return "NO_MAIL";
        return null;
    }
}
