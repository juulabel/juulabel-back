package com.juu.juulabel.domain.dto.terms;

import com.juu.juulabel.domain.enums.TermsType;

public record UsedTermsInfo(
    Long id,
    String title,
    String content,
    TermsType type,
    boolean isRequired
) {
}