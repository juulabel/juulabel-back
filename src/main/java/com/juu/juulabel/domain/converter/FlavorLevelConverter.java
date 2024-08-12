package com.juu.juulabel.domain.converter;


import com.juu.juulabel.domain.enums.alcohol.flavor.FlavorCommonLevel;
import jakarta.persistence.Converter;

@Converter
public class FlavorLevelConverter extends LevelConverter<FlavorCommonLevel> {

    public FlavorLevelConverter() {
        super(FlavorCommonLevel.class);
    }
}
