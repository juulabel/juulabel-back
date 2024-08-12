package com.juu.juulabel.domain.converter;


import com.juu.juulabel.domain.enums.alcohol.sensory.TurbidityLevel;
import jakarta.persistence.Converter;

@Converter
public class TurbidityLevelConverter extends LevelConverter<TurbidityLevel> {

    public TurbidityLevelConverter() {
        super(TurbidityLevel.class);
    }
}
