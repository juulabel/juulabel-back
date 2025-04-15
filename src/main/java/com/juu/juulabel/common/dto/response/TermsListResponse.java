package com.juu.juulabel.common.dto.response;


import com.juu.juulabel.terms.request.UsedTermsInfo;

import java.util.List;

public record TermsListResponse(
    List<UsedTermsInfo> usedTermsInfos
) {
}