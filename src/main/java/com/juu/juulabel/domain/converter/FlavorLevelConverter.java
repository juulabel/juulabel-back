package com.juu.juulabel.domain.converter;


import com.juu.juulabel.domain.enums.alcohol.flavor.FlavorLevel;
import jakarta.persistence.Converter;

@Converter
public class FlavorLevelConverter extends LevelConverter<FlavorLevel> {

    public FlavorLevelConverter() {
        super(FlavorLevel.class);
    }
}
