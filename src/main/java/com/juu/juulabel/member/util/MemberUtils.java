package com.juu.juulabel.member.util;

import com.juu.juulabel.alcohol.domain.AlcoholType;
import com.juu.juulabel.alcohol.repository.AlcoholTypeReader;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.MemberAlcoholType;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.Collections;

/**
 * Member 관련 유틸리티 클래스
 * 회원 가입 시 추가 데이터 처리를 담당
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MemberUtils {

    private final TermsReader termsReader;
    private final MemberAlcoholTypeWriter memberAlcoholTypeWriter;
    private final AlcoholTypeReader alcoholTypeReader;
    private final MemberTermsWriter memberTermsWriter;

    /**
     * 회원 가입 시 추가 데이터 처리 (주종, 약관 동의)
     * 트랜잭션 내에서 실행되어야 함
     */
    @Transactional
    public void processMemberData(Member member, SignUpMemberRequest signUpRequest) {

        try {
            // Process alcohol types if provided
            if (hasAlcoholTypes(signUpRequest)) {
                processAlcoholTypes(member, signUpRequest);
            }

            // Process terms agreements if provided
            if (hasTermsAgreements(signUpRequest)) {
                processTermsAgreements(member, signUpRequest);
            }

        } catch (InvalidParamException e) {

            throw e;
        } catch (Exception e) {

            throw new InvalidParamException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 회원-주종 관계 처리 (배치 처리 최적화)
     */
    private void processAlcoholTypes(Member member, SignUpMemberRequest signUpRequest) {
        List<Long> alcoholTypeIds = signUpRequest.alcoholTypeIds();

        // 중복 제거 및 유효성 검증
        List<Long> uniqueAlcoholTypeIds = alcoholTypeIds.stream()
                .distinct()
                .toList();

        if (uniqueAlcoholTypeIds.size() != alcoholTypeIds.size()) {
            throw new InvalidParamException(ErrorCode.EXCEEDED_FILE_COUNT);
        }

        List<MemberAlcoholType> memberAlcoholTypeList = createMemberAlcoholTypeList(
                member, uniqueAlcoholTypeIds);

        if (!memberAlcoholTypeList.isEmpty()) {
            memberAlcoholTypeWriter.storeAll(memberAlcoholTypeList);
        }
    }

    /**
     * 약관 동의 처리 (배치 처리 최적화)
     */
    private void processTermsAgreements(Member member, SignUpMemberRequest signUpRequest) {
        List<MemberTerms> memberTerms = validateAndCreateTermsAgreements(
                member, signUpRequest.termsAgreements());

        if (!memberTerms.isEmpty()) {
            memberTermsWriter.storeAll(memberTerms);
        }
    }

    /**
     * 회원-주종 관계 목록 생성 (예외 처리 강화)
     */
    private List<MemberAlcoholType> createMemberAlcoholTypeList(Member member, List<Long> alcoholTypeIds) {
        return alcoholTypeIds.stream()
                .map(alcoholTypeId -> {
                    try {
                        AlcoholType alcoholType = alcoholTypeReader.getById(alcoholTypeId);
                        return MemberAlcoholType.create(member, alcoholType);
                    } catch (Exception e) {
                        throw new InvalidParamException(ErrorCode.ALCOHOL_TYPE_NOT_FOUND);
                    }
                })
                .toList();
    }

    /**
     * 약관 동의 정보 검증 및 매핑 생성 (최적화)
     */
    private List<MemberTerms> validateAndCreateTermsAgreements(Member member, List<TermsAgreement> termsAgreements) {
        List<Terms> activeTermsList = termsReader.getAllByIsUsed();

        if (activeTermsList.isEmpty()) {

            return Collections.emptyList();
        }

        validateTermsAgreements(activeTermsList, termsAgreements);
        return createMemberTermsList(member, activeTermsList, termsAgreements);
    }

    /**
     * 약관 동의 검증 (성능 최적화)
     */
    private void validateTermsAgreements(List<Terms> activeTermsList, List<TermsAgreement> termsAgreements) {
        Map<Long, TermsAgreement> agreementMap = termsAgreements.stream()
                .collect(Collectors.toMap(TermsAgreement::termsId, Function.identity()));

        // 모든 활성 약관에 대한 동의가 있는지 확인 (조기 종료 최적화)
        boolean hasMissingAgreement = activeTermsList.stream()
                .map(Terms::getId)
                .anyMatch(termsId -> !agreementMap.containsKey(termsId));

        if (hasMissingAgreement) {
            throw new InvalidParamException(ErrorCode.TERMS_AGREEMENT_MISMATCH);
        }

        // 필수 약관 동의 확인 (조기 종료 최적화)
        boolean hasUnagreedRequiredTerms = activeTermsList.stream()
                .filter(Terms::isRequired)
                .anyMatch(terms -> {
                    TermsAgreement agreement = agreementMap.get(terms.getId());
                    return agreement == null || !agreement.isAgreed();
                });

        if (hasUnagreedRequiredTerms) {
            throw new InvalidParamException(ErrorCode.TERMS_AGREEMENT_MISSING_REQUIRED);
        }
    }

    /**
     * 회원-약관 관계 목록 생성
     */
    private List<MemberTerms> createMemberTermsList(Member member, List<Terms> activeTermsList,
            List<TermsAgreement> termsAgreements) {

        Map<Long, TermsAgreement> agreementMap = termsAgreements.stream()
                .collect(Collectors.toMap(TermsAgreement::termsId, Function.identity()));

        LocalDateTime now = LocalDateTime.now();

        return activeTermsList.stream()
                .map(terms -> {
                    TermsAgreement agreement = agreementMap.get(terms.getId());
                    return MemberTerms.create(member, terms, agreement.isAgreed(), now);
                })
                .toList();
    }

    public List<MemberAlcoholType> getMemberAlcoholTypeList(Member member, List<Long> alcoholTypeIdList,
            AlcoholTypeReader alcoholTypeReader) {
        return alcoholTypeIdList.stream()
                .map(alcoholTypeId -> {
                    AlcoholType alcoholType = alcoholTypeReader.getById(alcoholTypeId);
                    return MemberAlcoholType.create(member, alcoholType);
                })
                .toList();
    }

    private boolean hasAlcoholTypes(SignUpMemberRequest signUpRequest) {
        return signUpRequest.alcoholTypeIds() != null && !signUpRequest.alcoholTypeIds().isEmpty();
    }

    private boolean hasTermsAgreements(SignUpMemberRequest signUpRequest) {
        return signUpRequest.termsAgreements() != null && !signUpRequest.termsAgreements().isEmpty();
    }
}