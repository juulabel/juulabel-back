package com.juu.juulabel.api.dto.response;

import com.juu.juulabel.domain.dto.terms.UsedTermsInfo;

import java.util.List;

public record TermsListResponse(
    List<UsedTermsInfo> usedTermsInfos
) {
}