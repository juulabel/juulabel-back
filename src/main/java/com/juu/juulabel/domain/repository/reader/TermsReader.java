package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.api.dto.response.TermsDetailResponse;
import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.dto.terms.UsedTermsInfo;
import com.juu.juulabel.domain.entity.terms.Terms;
import com.juu.juulabel.domain.repository.query.TermsQueryRepository;
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