package com.juu.juulabel.member.util;

import com.juu.juulabel.alcohol.domain.AlcoholType;
import com.juu.juulabel.alcohol.repository.AlcoholTypeReader;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.MemberAlcoholType;

import java.util.List;

import org.springframework.stereotype.Component;

import com.juu.juulabel.common.dto.request.SignUpMemberRequest;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.member.domain.MemberTerms;
import com.juu.juulabel.member.repository.MemberAlcoholTypeWriter;
import com.juu.juulabel.member.repository.MemberTermsWriter;
import com.juu.juulabel.member.repository.TermsReader;
import com.juu.juulabel.terms.domain.Terms;
import com.juu.juulabel.terms.request.TermsAgreement;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.Collections;

/**
 * Member 관련 유틸리티 클래스
 */
@Component
@RequiredArgsConstructor
public class MemberUtils {

    /**
     * 회원-주종 관계 목록 생성
     * 
     * @param member            회원
     * @param alcoholTypeIdList 주종 ID 목록
     * @param alcoholTypeReader 주종 조회 리포지토리
     * @return 회원-주종 관계 목록
     */
    private final TermsReader termsReader;
    private final MemberAlcoholTypeWriter memberAlcoholTypeWriter;
    private final AlcoholTypeReader alcoholTypeReader;
    private final MemberTermsWriter memberTermsWriter;

    public List<MemberAlcoholType> getMemberAlcoholTypeList(Member member, List<Long> alcoholTypeIdList,
            AlcoholTypeReader alcoholTypeReader) {
        return alcoholTypeIdList.stream()
                .map(alcoholTypeId -> {
                    AlcoholType alcoholType = alcoholTypeReader.getById(alcoholTypeId);
                    return MemberAlcoholType.create(member, alcoholType);
                })
                .toList();
    }

    public void processAlcoholTypes(Member member, SignUpMemberRequest signUpRequest) {
        List<MemberAlcoholType> memberAlcoholTypeList = getMemberAlcoholTypeList(
                member, signUpRequest.alcoholTypeIds(), alcoholTypeReader);
        if (!memberAlcoholTypeList.isEmpty()) {
            memberAlcoholTypeWriter.storeAll(memberAlcoholTypeList);
        }
    }

    public void processTermsAgreements(Member member, SignUpMemberRequest signUpRequest) {
        List<MemberTerms> memberTerms = getAndValidateTermsWithMapping(member,
                signUpRequest.termsAgreements());
        if (!memberTerms.isEmpty()) {
            memberTermsWriter.storeAll(memberTerms);
        }
    }

    /**
     * 약관 동의 정보 검증 및 매핑 생성
     */
    public List<MemberTerms> getAndValidateTermsWithMapping(Member member, List<TermsAgreement> termsAgreements) {
        List<Terms> usedTermsList = termsReader.getAllByIsUsed();

        if (usedTermsList.isEmpty()) {
            return Collections.emptyList();
        }

        validateTermsList(usedTermsList, termsAgreements);
        return createMemberTermsList(member, usedTermsList, termsAgreements);
    }

    public List<MemberTerms> createMemberTermsList(Member member, List<Terms> usedTermsList,
            List<TermsAgreement> termsAgreements) {

        // 약관 ID를 키로 하는 맵으로 변환하여 조회 성능 개선
        Map<Long, TermsAgreement> agreementMap = termsAgreements.stream()
                .collect(Collectors.toMap(TermsAgreement::termsId, Function.identity()));

        final LocalDateTime now = LocalDateTime.now();
        List<MemberTerms> mappings = new ArrayList<>(usedTermsList.size());

        for (Terms terms : usedTermsList) {
            TermsAgreement termsAgreement = Optional.ofNullable(agreementMap.get(terms.getId()))
                    .orElseThrow(() -> new InvalidParamException(ErrorCode.TERMS_NOT_FOUND));

            final boolean isAgreed = termsAgreement.isAgreed();

            if (terms.isRequired() && !isAgreed) {
                throw new InvalidParamException(ErrorCode.TERMS_AGREEMENT_MISSING_REQUIRED);
            }

            mappings.add(MemberTerms.create(member, terms, isAgreed, now));
        }

        return mappings;
    }

    public void validateTermsList(List<Terms> usedTermsList, List<TermsAgreement> termsAgreements) {
        if (usedTermsList.size() != termsAgreements.size()) {
            throw new InvalidParamException(ErrorCode.TERMS_AGREEMENT_MISMATCH);
        }
    }
}