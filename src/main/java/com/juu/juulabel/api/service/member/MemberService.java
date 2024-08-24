package com.juu.juulabel.api.service.member;

import com.juu.juulabel.api.dto.request.OAuthLoginRequest;
import com.juu.juulabel.api.dto.request.SignUpMemberRequest;
import com.juu.juulabel.api.dto.request.UpdateProfileRequest;
import com.juu.juulabel.api.dto.response.LoginResponse;
import com.juu.juulabel.api.dto.response.SignUpMemberResponse;
import com.juu.juulabel.api.dto.response.UpdateProfileResponse;
import com.juu.juulabel.api.factory.OAuthProviderFactory;
import com.juu.juulabel.api.provider.JwtTokenProvider;
import com.juu.juulabel.api.service.s3.S3Service;
import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.dto.member.OAuthLoginInfo;
import com.juu.juulabel.domain.dto.member.OAuthUser;
import com.juu.juulabel.domain.dto.member.OAuthUserInfo;
import com.juu.juulabel.domain.dto.s3.UploadImageInfo;
import com.juu.juulabel.domain.dto.terms.TermsAgreement;
import com.juu.juulabel.domain.dto.token.Token;
import com.juu.juulabel.domain.entity.alcohol.AlcoholType;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.member.MemberAlcoholType;
import com.juu.juulabel.domain.entity.member.MemberTerms;
import com.juu.juulabel.domain.entity.terms.Terms;
import com.juu.juulabel.domain.enums.member.Provider;
import com.juu.juulabel.domain.repository.reader.AlcoholTypeReader;
import com.juu.juulabel.domain.repository.reader.MemberReader;
import com.juu.juulabel.domain.repository.reader.TermsReader;
import com.juu.juulabel.domain.repository.writer.MemberAlcoholTypeWriter;
import com.juu.juulabel.domain.repository.writer.MemberTermsWriter;
import com.juu.juulabel.domain.repository.writer.MemberWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final OAuthProviderFactory providerFactory;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberReader memberReader;
    private final MemberWriter memberWriter;
    private final TermsReader termsReader;
    private final MemberTermsWriter memberTermsWriter;
    private final MemberAlcoholTypeWriter memberAlcoholTypeWriter;
    private final AlcoholTypeReader alcoholTypeReader;
    private final S3Service s3Service;


    @Transactional
    public LoginResponse login(OAuthLoginRequest oAuthLoginRequest) {
        OAuthLoginInfo authLoginInfo = oAuthLoginRequest.toDto();
        Provider provider = authLoginInfo.provider();

        // 인가 코드를 이용해 토큰 발급 요청
        String accessToken = providerFactory.getAccessToken(
            provider,
            authLoginInfo.propertyMap().get(AuthConstants.REDIRECT_URI),
            authLoginInfo.propertyMap().get(AuthConstants.CODE)
        );

        // 토큰을 이용해 사용자 정보 가져오기
        OAuthUser oAuthUser = providerFactory.getOAuthUser(provider, accessToken);

        // 회원가입 or 로그인
        String email = oAuthUser.email();
        boolean isNewMember = !memberReader.existsByEmailAndProvider(email, provider);

        Token token = createTokenForMember(isNewMember, email); // TODO : 카카오와 구글 이메일이 같다면 토큰 중복 사용 가능 여부 확인

        return new LoginResponse(
            token,
            isNewMember,
            new OAuthUserInfo(email, oAuthUser.id(), provider)
        );
    }

    @Transactional
    public SignUpMemberResponse signUp(SignUpMemberRequest signUpRequest) { // TODO : providerId 검증
        validateEmail(signUpRequest.email());
        validateNickname(signUpRequest.nickname());

        Member member = Member.create(signUpRequest);
        memberWriter.store(member);

        // 선호전통주 주종 등록
        List<MemberAlcoholType> memberAlcoholTypeList =
            getMemberAlcoholTypeList(member, signUpRequest.alcoholTypeIds());
        if (!memberAlcoholTypeList.isEmpty()) {
            memberAlcoholTypeWriter.storeAll(memberAlcoholTypeList);
        }

        // 약관 등록
        List<MemberTerms> memberTerms =
            getAndValidateTermsWithMapping(member, signUpRequest.termsAgreements());
        if (!memberTerms.isEmpty()) {
            memberTermsWriter.storeAll(memberTerms);
        }

        String token = jwtTokenProvider.createAccessToken(member.getEmail());

        return new SignUpMemberResponse(
            member.getId(),
            new Token(token, jwtTokenProvider.getExpirationByToken(token))
        );
    }

    private Token createTokenForMember(boolean isNewMember, String email) {
        if (isNewMember) {
            return new Token(null, null);
        } else {
            String generatedToken = jwtTokenProvider.createAccessToken(email);
            return new Token(generatedToken, jwtTokenProvider.getExpirationByToken(generatedToken));
        }
    }

    private List<MemberAlcoholType> getMemberAlcoholTypeList(Member member, List<Long> alcoholTypeIdList) {
        return alcoholTypeIdList.stream()
            .map(alcoholTypeId -> {
                AlcoholType alcoholType = alcoholTypeReader.getById(alcoholTypeId);
                return MemberAlcoholType.create(member, alcoholType);
            })
            .toList();
    }

    private List<MemberTerms> getAndValidateTermsWithMapping(Member member, List<TermsAgreement> termsAgreements) {
        List<Terms> usedTermsList = termsReader.getAllByIsUsed();
        // 사용중인 약관이 존재하지 않을 경우 생성하지 않는다.
        if (!usedTermsList.isEmpty()) {
            validateTermsList(usedTermsList, termsAgreements);
        }

        return getMemberTermsList(member, usedTermsList, termsAgreements);
    }

    private void validateTermsList(List<Terms> usedTermsList, List<TermsAgreement> termsAgreements) {
        if (usedTermsList.size() != termsAgreements.size()) {
            throw new InvalidParamException(ErrorCode.MISMATCH_TERMS_AGREEMENT);
        }
    }

    private List<MemberTerms> getMemberTermsList(Member member, List<Terms> usedTermsList, List<TermsAgreement> termsAgreements) {
        List<MemberTerms> mappings = new ArrayList<>();
        final LocalDateTime now = LocalDateTime.now();

        usedTermsList.forEach(terms -> {
            TermsAgreement termsAgreement = termsAgreements.stream()
                .filter(agreement -> agreement.termsId().equals(terms.getId()))
                .findFirst()
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_TERMS));

            final boolean isAgreed = termsAgreement.isAgreed();

            if (terms.isRequired() && !isAgreed) {
                throw new InvalidParamException(ErrorCode.MISSING_REQUIRED_AGREEMENT);
            }

            mappings.add(MemberTerms.create(member, terms, isAgreed, now));
        });

        return mappings;
    }

    @Transactional(readOnly = true)
    public boolean checkNickname(String nickname) {
        return memberReader.existActiveNickname(nickname);
    }

    @Transactional
    public UpdateProfileResponse updateProfile(Member loginMember, UpdateProfileRequest request, MultipartFile image) {
        Member member = memberReader.getByEmail(loginMember.getEmail());
        UploadImageInfo uploadImageInfo = s3Service.uploadMemberProfileImage(image);
        member.updateProfile(request, uploadImageInfo.ImageUrl());

        memberAlcoholTypeWriter.deleteAllByMember(member);

        List<MemberAlcoholType> memberAlcoholTypeList = getMemberAlcoholTypeList(member, request.alcoholTypeIds());
        if (!memberAlcoholTypeList.isEmpty()) {
            memberAlcoholTypeWriter.storeAll(memberAlcoholTypeList);
        }

        return new UpdateProfileResponse(member.getId());
    }

    private void validateNickname(String nickname) {
        if (memberReader.existActiveNickname(nickname)) {
            throw new InvalidParamException(ErrorCode.EXIST_NICKNAME);
        }
    }

    private void validateEmail(String email) {
        if (memberReader.existActiveEmail(email)) {
            throw new InvalidParamException(ErrorCode.EXIST_EMAIL);
        }
    }

}

