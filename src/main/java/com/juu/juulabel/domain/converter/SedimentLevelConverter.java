package com.juu.juulabel.domain.converter;


import com.juu.juulabel.domain.enums.alcohol.sensory.SedimentLevel;
import jakarta.persistence.Converter;

@Converter
public class SedimentLevelConverter extends LevelConverter<SedimentLevel> {

    public SedimentLevelConverter() {
        super(SedimentLevel.class);
    }
}
