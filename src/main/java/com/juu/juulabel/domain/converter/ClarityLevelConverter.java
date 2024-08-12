package com.juu.juulabel.domain.converter;


import com.juu.juulabel.domain.enums.alcohol.sensory.ClarityLevel;
import jakarta.persistence.Converter;

@Converter
public class ClarityLevelConverter extends LevelConverter<ClarityLevel> {

    public ClarityLevelConverter() {
        super(ClarityLevel.class);
    }
}
