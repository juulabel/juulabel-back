package com.juu.juulabel.domain.enums;

import com.juu.juulabel.domain.dto.alcohol.Level;

import java.util.Arrays;
import java.util.List;

/**
 * description : interface used to grade properties (e.g. carbonation, viscosity, etc.)
 */
public interface Rateable {
    Integer getScore();
    String getDescription();

    default List<Level> getLevels() {
        return Arrays.stream(getClass().getEnumConstants())
                .map(enumConstant -> new Level(enumConstant.getScore(), enumConstant.getDescription()))
                .toList();
    }

    static <E extends Enum<E> & Rateable> List<Level> getLevels(Class<E> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(rateable -> new Level(rateable.getScore(), rateable.getDescription()))
                .toList();
    }

}