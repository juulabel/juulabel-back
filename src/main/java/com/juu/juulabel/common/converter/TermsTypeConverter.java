package com.juu.juulabel.common.converter;


import com.juu.juulabel.terms.domain.TermsType;
import jakarta.persistence.Converter;

@Converter
public class TermsTypeConverter extends CodeConverter<TermsType> {

    public TermsTypeConverter() {
        super(TermsType.class);
    }
}
