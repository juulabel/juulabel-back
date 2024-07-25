package com.juu.juulabel.domain.dto.terms;

import com.juu.juulabel.domain.enums.terms.TermsType;

public record UsedTermsInfo(
    Long id,
    String title,
    TermsType type,
    boolean isRequired
) {
}