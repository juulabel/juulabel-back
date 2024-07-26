package com.juu.juulabel.domain.converter;


import com.juu.juulabel.domain.enums.alcohol.sensory.CarbonationLevel;
import jakarta.persistence.Converter;

@Converter
public class CarbonationLevelConverter extends LevelConverter<CarbonationLevel> {

    public CarbonationLevelConverter() {
        super(CarbonationLevel.class);
    }
}
