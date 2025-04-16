package com.juu.juulabel.terms.request;

import com.juu.juulabel.terms.domain.TermsType;

public record UsedTermsInfo(
    Long id,
    String title,
    TermsType type,
    boolean isRequired
) {
}