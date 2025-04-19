package com.juu.juulabel.member.repository;

import com.juu.juulabel.common.dto.response.TermsDetailResponse;
import com.juu.juulabel.common.annotation.Reader;
import com.juu.juulabel.terms.domain.Terms;
import com.juu.juulabel.member.repository.query.TermsQueryRepository;
import com.juu.juulabel.terms.request.UsedTermsInfo;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Reader
@RequiredArgsConstructor
public class TermsReader {

    private final TermsQueryRepository termsQueryRepository;

    public List<Terms> getAllByIsUsed() {
        return termsQueryRepository.getAllByIsUsed();
    }

    public List<UsedTermsInfo> getAllUsedTerms() {
        return termsQueryRepository.getAllUsedTerms();
    }

    public TermsDetailResponse getTermsById(Long termsId) {
        return termsQueryRepository.getTermsById(termsId);
    }
}