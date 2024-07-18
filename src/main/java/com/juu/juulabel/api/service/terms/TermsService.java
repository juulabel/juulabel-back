package com.juu.juulabel.api.service.terms;

import com.juu.juulabel.api.dto.response.LoadTermsListResponse;
import com.juu.juulabel.api.dto.response.TermsDetailResponse;
import com.juu.juulabel.domain.dto.terms.UsedTermsInfo;
import com.juu.juulabel.domain.repository.reader.TermsReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TermsService {

    private final TermsReader termsReader;

    public LoadTermsListResponse loadUsedTermsList() {
        List<UsedTermsInfo> termsInfoList = termsReader.getAllUsedTerms();
        return new LoadTermsListResponse(termsInfoList);
    }

    public TermsDetailResponse loadTermsDetail(Long termsId) {
        return termsReader.getTermsById(termsId);
    }
}