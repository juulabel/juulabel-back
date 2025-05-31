package com.juu.juulabel.common.dto.request;

import com.juu.juulabel.member.domain.Gender;
import com.juu.juulabel.terms.request.TermsAgreement;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SignUpMemberRequest(
        @NotBlank(message = "닉네임이 누락되었습니다.") String nickname,
        @NotNull(message = "성별이 누락되었습니다.") Gender gender,
        @NotEmpty(message = "주종 선택값이 누락되었습니다.") List<Long> alcoholTypeIds,
        List<TermsAgreement> termsAgreements) {
}