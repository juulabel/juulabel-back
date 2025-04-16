package com.juu.juulabel.domain.converter;

import com.juu.juulabel.domain.enums.Code;
import jakarta.persistence.AttributeConverter;
import lombok.AllArgsConstructor;

import java.util.EnumSet;
import java.util.NoSuchElementException;

@AllArgsConstructor
public class CodeConverter<E extends Enum<E> & Code> implements AttributeConverter<E, String> {

    private final Class<E> target;

    @Override
    public String convertToDatabaseColumn(E code) {
        return code.getCode();
    }

    @Override
    public E convertToEntityAttribute(String code) {
        return EnumSet.allOf(target).stream()
                .filter(e -> e.getCode().equals(code))
                .findAny()
                .orElseThrow(NoSuchElementException::new);
    }

}
