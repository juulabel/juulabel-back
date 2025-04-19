package com.juu.juulabel.common.dto.response;

import com.juu.juulabel.terms.domain.TermsType;

public record TermsDetailResponse(
    Long id,
    String title,
    String content,
    TermsType type,
    boolean isRequired
) {
}