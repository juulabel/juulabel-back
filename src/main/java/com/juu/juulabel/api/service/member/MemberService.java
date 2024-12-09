package com.juu.juulabel.api.service.member;

import com.juu.juulabel.api.dto.request.*;
import com.juu.juulabel.api.dto.response.*;
import com.juu.juulabel.api.factory.OAuthProviderFactory;
import com.juu.juulabel.api.provider.JwtTokenProvider;
import com.juu.juulabel.api.service.s3.S3Service;
import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksSummary;
import com.juu.juulabel.domain.dto.dailylife.DailyLifeSummary;
import com.juu.juulabel.domain.dto.dailylife.MyDailyLifeSummary;
import com.juu.juulabel.domain.dto.member.OAuthLoginInfo;
import com.juu.juulabel.domain.dto.member.OAuthUser;
import com.juu.juulabel.domain.dto.member.OAuthUserInfo;
import com.juu.juulabel.domain.dto.s3.UploadImageInfo;
import com.juu.juulabel.domain.dto.tastingnote.MyTastingNoteSummary;
import com.juu.juulabel.domain.dto.tastingnote.TastingNoteSummary;
import com.juu.juulabel.domain.dto.terms.TermsAgreement;
import com.juu.juulabel.domain.dto.token.Token;
import com.juu.juulabel.domain.entity.alcohol.AlcoholType;
import com.juu.juulabel.domain.entity.alcohol.AlcoholicDrinks;
import com.juu.juulabel.domain.entity.member.*;
import com.juu.juulabel.domain.entity.terms.Terms;
import com.juu.juulabel.domain.enums.member.Provider;
import com.juu.juulabel.domain.repository.reader.*;
import com.juu.juulabel.domain.repository.writer.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private final MemberAlcoholTypeReader memberAlcoholTypeReader;
    private final AlcoholTypeReader alcoholTypeReader;
    private final S3Service s3Service;
    private final DailyLifeReader dailyLifeReader;
    private final AlcoholicDrinksReader alcoholicDrinksReader;
    private final MemberAlcoholicDrinksReader memberAlcoholicDrinksReader;
    private final MemberAlcoholicDrinksWriter memberAlcoholicDrinksWriter;
    private final TastingNoteReader tastingNoteReader;
    private final WithdrawalRecordWriter withdrawalRecordWriter;
    private final WithdrawalRecordReader withdrawalRecordReader;


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

        validateNotWithdrawnMember(email);

        Token token = createTokenForMember(isNewMember, email); // TODO : 카카오와 구글 이메일이 같다면 토큰 중복 사용 가능 여부 확인

        return new LoginResponse(
            token,
            isNewMember,
            new OAuthUserInfo(email, oAuthUser.id(), provider)
        );
    }

    @Transactional
    public SignUpMemberResponse signUp(SignUpMemberRequest signUpRequest) { // TODO : providerId 검증
        validateNickname(signUpRequest.nickname());
        validateEmail(signUpRequest.email());

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

        String profileImageUrl = null;
        if (image != null) {
            UploadImageInfo uploadImageInfo = s3Service.uploadMemberProfileImage(image);
            profileImageUrl = uploadImageInfo.ImageUrl();
        }
        member.updateProfile(request, profileImageUrl);

        memberAlcoholTypeWriter.deleteAllByMember(member);

        List<MemberAlcoholType> memberAlcoholTypeList = getMemberAlcoholTypeList(member, request.alcoholTypeIds());
        if (!memberAlcoholTypeList.isEmpty()) {
            memberAlcoholTypeWriter.storeAll(memberAlcoholTypeList);
        }

        return new UpdateProfileResponse(member.getId());
    }

    @Transactional(readOnly = true)
    public MyDailyLifeListResponse loadMyDailyLifeList(Member member, DailyLifeListRequest request) {
        Slice<MyDailyLifeSummary> myDailyLifeList =
            dailyLifeReader.getAllMyDailyLives(member, request.lastDailyLifeId(), request.pageSize());

        return new MyDailyLifeListResponse(myDailyLifeList);
    }

    @Transactional(readOnly = true)
    public MyTastingNoteListResponse loadMyTastingNoteList(Member member, TastingNoteListRequest request) {
        Slice<MyTastingNoteSummary> myTastingNoteList =
            tastingNoteReader.getAllMyTastingNotes(member, request.lastTastingNoteId(), request.pageSize());

        return new MyTastingNoteListResponse(myTastingNoteList);
    }

    @Transactional
    public boolean saveAlcoholicDrinks(Member member, Long alcoholicDrinksId) {
        AlcoholicDrinks alcoholicDrinks = alcoholicDrinksReader.getById(alcoholicDrinksId);
        Optional<MemberAlcoholicDrinks> memberAlcoholicDrinks =
            memberAlcoholicDrinksReader.findByMemberAndAlcoholicDrinks(member, alcoholicDrinks);

        // 전통주가 이미 저장되어 있다면 삭제, 저장되어 있지 않다면 등록
        return memberAlcoholicDrinks
            .map(save -> {
                memberAlcoholicDrinksWriter.delete(save);
                return false;
            })
            .orElseGet(() -> {
                memberAlcoholicDrinksWriter.store(member, alcoholicDrinks);
                return true;
            });
    }

    @Transactional(readOnly = true)
    public MyAlcoholicDrinksListResponse loadMyAlcoholicDrinks(Member member, MyAlcoholicDrinksListRequest request) {
        Slice<AlcoholicDrinksSummary> alcoholicDrinksSummaries =
            alcoholicDrinksReader.getAllMyAlcoholicDrinks(member, request.lastAlcoholicDrinksId(), request.pageSize());

        return new MyAlcoholicDrinksListResponse(alcoholicDrinksSummaries);
    }

    @Transactional(readOnly = true)
    public MySpaceResponse getMySpace(Member member) {
        long tastingNoteCount = tastingNoteReader.getMyTastingNoteCount(member);
        long dailyLifeCount = dailyLifeReader.getMyDailyLifeCount(member);
        return new MySpaceResponse(
            member.getProfileImage(),
            member.getNickname(),
            member.getIntroduction(),
            tastingNoteCount,
            dailyLifeCount,
            0 // TODO : 시음노트 저장 기능 추가 시 수정 필요
        );
    }

    @Transactional(readOnly = true)
    public MyInfoResponse getMyInfo(Member member) {
        List<Long> alcoholTypeIdList = memberAlcoholTypeReader.getIdListByMember(member);
        return new MyInfoResponse(
            member.getId(),
            member.getNickname(),
            member.getEmail(),
            member.isNotificationsAllowed(),
            member.getIntroduction(),
            member.getProfileImage(),
            member.getGender(),
            alcoholTypeIdList
        );
    }

    @Transactional(readOnly = true)
    public DailyLifeListResponse loadMemberDailyLifeList(Member loginMember, DailyLifeListRequest request, Long memberId) {
        Slice<DailyLifeSummary> dailyLifeList =
            dailyLifeReader.getAllDailyLivesByMember(loginMember, memberId, request.lastDailyLifeId(), request.pageSize());

        return new DailyLifeListResponse(dailyLifeList);
    }

    @Transactional(readOnly = true)
    public TastingNoteListResponse loadMemberTastingNoteList(Member loginMember, TastingNoteListRequest request, Long memberId) {
        // TODO : 해당 회원(loginMember) 차단 여부 검증 로직
        Slice<TastingNoteSummary> tastingNoteList =
            tastingNoteReader.getAllTastingNotesByMember(loginMember, memberId, request.lastTastingNoteId(), request.pageSize());

        return new TastingNoteListResponse(tastingNoteList);
    }

    @Transactional(readOnly = true)
    public MemberProfileResponse getMemberProfile(Member loginMember, Long memberId) {
        // TODO : 해당 회원(loginMember) 차단 여부 검증 로직
        Member member = memberReader.getById(memberId);
        long tastingNoteCount = tastingNoteReader.getTastingNoteCountByMemberId(memberId, loginMember);
        long dailyLifeCount = dailyLifeReader.getDailyLifeCountByMemberId(memberId, loginMember);
        return new MemberProfileResponse(
            member.getId(),
            member.getNickname(),
            member.getProfileImage(),
            member.getIntroduction(),
            tastingNoteCount,
            dailyLifeCount
        );
    }

    private void validateNickname(String nickname) {
        if (memberReader.existActiveNickname(nickname)) {
            throw new InvalidParamException(ErrorCode.EXIST_NICKNAME);
        }
    }

    private void validateNotWithdrawnMember(String email) {
        if (withdrawalRecordReader.existEmail(email)) {
            throw new InvalidParamException(ErrorCode.WITHDRAWN_MEMBER);
        }
    }

    private void validateEmail(String email) {
        if (memberReader.existActiveEmail(email)) {
            throw new InvalidParamException(ErrorCode.EXIST_EMAIL);
        }
    }

    @Transactional
    public void deleteAccount(Member loginMember, WithdrawalRequest request) {
        loginMember.deleteAccount();
        withdrawalRecordWriter.store(
            WithdrawalRecord.create(request.withdrawalReason(), loginMember.getEmail(), loginMember.getNickname())
        );
    }
}

