package com.juu.juulabel.domain.converter;


import com.juu.juulabel.domain.enums.TermsType;
import jakarta.persistence.Converter;

@Converter
public class TermsTypeConverter extends CodeConverter<TermsType> {

    public TermsTypeConverter() {
        super(TermsType.class);
    }
}