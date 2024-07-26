package com.juu.juulabel.domain.converter;


import com.juu.juulabel.domain.enums.alcohol.sensory.ViscosityLevel;
import jakarta.persistence.Converter;

@Converter
public class ViscosityLevelConverter extends LevelConverter<ViscosityLevel> {

    public ViscosityLevelConverter() {
        super(ViscosityLevel.class);
    }
}
