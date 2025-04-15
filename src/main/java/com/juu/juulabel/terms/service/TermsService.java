package com.juu.juulabel.terms.service;

import com.juu.juulabel.common.dto.response.TermsDetailResponse;
import com.juu.juulabel.common.dto.response.TermsListResponse;
import com.juu.juulabel.member.repository.TermsReader;
import com.juu.juulabel.terms.request.UsedTermsInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TermsService {

    private final TermsReader termsReader;

    public TermsListResponse loadUsedTermsList() {
        List<UsedTermsInfo> termsInfoList = termsReader.getAllUsedTerms();
        return new TermsListResponse(termsInfoList);
    }

    public TermsDetailResponse loadTermsDetail(Long termsId) {
        return termsReader.getTermsById(termsId);
    }
}