package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.enums.TermsType;

public record TermsDetailResponse(
    Long id,
    String title,
    String content,
    TermsType type,
    boolean isRequired
) {
}