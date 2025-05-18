package com.juu.juulabel.auth.service;

import com.juu.juulabel.auth.repository.redis.RefreshTokenRedisRepository;
import com.juu.juulabel.common.dto.request.SignUpMemberRequest;
import com.juu.juulabel.common.dto.request.WithdrawalRequest;
import com.juu.juulabel.common.dto.response.SignUpMemberResponse;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.provider.JwtTokenProvider;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.WithdrawalRecord;
import com.juu.juulabel.member.repository.MemberReader;
import com.juu.juulabel.member.repository.MemberWriter;
import com.juu.juulabel.member.repository.WithdrawalRecordWriter;
import com.juu.juulabel.member.token.Token;
import com.juu.juulabel.member.util.MemberUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberAuthService {
    private final MemberReader memberReader;
    private final MemberWriter memberWriter;
    private final WithdrawalRecordWriter withdrawalRecordWriter;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberUtils memberUtils;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Transactional
    public SignUpMemberResponse signUp(SignUpMemberRequest signUpRequest) {
        validateNickname(signUpRequest.nickname());
        validateEmail(signUpRequest.email());

        Member member = Member.create(signUpRequest);
        memberWriter.store(member);

        memberUtils.processAlcoholTypes(member, signUpRequest);
        memberUtils.processTermsAgreements(member, signUpRequest);

        String token = jwtTokenProvider.createAccessToken(member);

        return new SignUpMemberResponse(
                member.getId(),
                new Token(token, jwtTokenProvider.getExpirationByToken(token)));
    }

    @Transactional
    public void deleteAccount(Member loginMember, WithdrawalRequest request) {
        loginMember.deleteAccount();
        withdrawalRecordWriter.store(
                WithdrawalRecord.create(request.withdrawalReason(), loginMember.getEmail(), loginMember.getNickname()));
        refreshTokenRedisRepository.revokeByMemberId(loginMember.getId());
    }

    private void validateNickname(String nickname) {
        if (memberReader.existActiveNickname(nickname)) {
            throw new InvalidParamException(ErrorCode.MEMBER_NICKNAME_DUPLICATE);
        }
    }

    private void validateEmail(String email) {
        if (memberReader.existActiveEmail(email)) {
            throw new InvalidParamException(ErrorCode.MEMBER_EMAIL_DUPLICATE);
        }
    }
}