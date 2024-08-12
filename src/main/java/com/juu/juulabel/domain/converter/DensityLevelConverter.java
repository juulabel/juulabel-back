package com.juu.juulabel.domain.converter;


import com.juu.juulabel.domain.enums.alcohol.sensory.DensityLevel;
import jakarta.persistence.Converter;

@Converter
public class DensityLevelConverter extends LevelConverter<DensityLevel> {

    public DensityLevelConverter() {
        super(DensityLevel.class);
    }
}
