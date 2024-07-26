package com.juu.juulabel.domain.converter;

import com.juu.juulabel.domain.enums.Rateable;
import jakarta.persistence.AttributeConverter;
import lombok.AllArgsConstructor;

import java.util.EnumSet;
import java.util.NoSuchElementException;
import java.util.Objects;

@AllArgsConstructor
public class LevelConverter<E extends Enum<E> & Rateable> implements AttributeConverter<E, Integer> {

    private final Class<E> target;

    @Override
    public Integer convertToDatabaseColumn(E level) {
        if (Objects.isNull(level)) {
            return null;
        }

        return level.getScore();
    }

    @Override
    public E convertToEntityAttribute(Integer score) {
        if (Objects.isNull(score)) {
            return null;
        }

        return EnumSet.allOf(target).stream()
                .filter(e -> Objects.equals(e.getScore(), score))
                .findAny()
                .orElseThrow(NoSuchElementException::new);
    }
}
