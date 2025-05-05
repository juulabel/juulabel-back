package com.juu.juulabel.member.service;

import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.common.dto.request.OAuthLoginRequest;
import com.juu.juulabel.common.dto.request.SignUpMemberRequest;
import com.juu.juulabel.common.dto.request.WithdrawalRequest;
import com.juu.juulabel.common.dto.response.LoginResponse;
import com.juu.juulabel.common.dto.response.RefreshResponse;
import com.juu.juulabel.common.dto.response.SignUpMemberResponse;
import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.factory.OAuthProviderFactory;
import com.juu.juulabel.common.provider.JwtTokenProvider;
import com.juu.juulabel.common.util.HttpRequestUtil;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.Provider;
import com.juu.juulabel.member.domain.RefreshToken;
import com.juu.juulabel.member.domain.WithdrawalRecord;
import com.juu.juulabel.member.repository.MemberReader;
import com.juu.juulabel.member.repository.MemberWriter;
import com.juu.juulabel.member.repository.RefreshTokenRepository;
import com.juu.juulabel.member.repository.WithdrawalRecordReader;
import com.juu.juulabel.member.repository.WithdrawalRecordWriter;
import com.juu.juulabel.member.request.OAuthLoginInfo;
import com.juu.juulabel.member.request.OAuthUser;
import com.juu.juulabel.member.request.OAuthUserInfo;
import com.juu.juulabel.member.token.Token;
import com.juu.juulabel.member.util.MemberUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.juu.juulabel.common.constants.AuthConstants.REFRESH_TOKEN_DURATION;
import static com.juu.juulabel.common.constants.AuthConstants.REFRESH_TOKEN_HEADER_NAME;

/**
 * 인증 및 토큰 관리 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final OAuthProviderFactory providerFactory;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberReader memberReader;
    private final MemberWriter memberWriter;
    private final WithdrawalRecordReader withdrawalRecordReader;
    private final WithdrawalRecordWriter withdrawalRecordWriter;
    private final RefreshTokenRepository refreshTokenRepository;
    private final HttpRequestUtil httpRequestUtil;
    private final MemberUtils memberUtils;

    // ===== 인증 관련 메서드 =====

    /**
     * OAuth 로그인 처리
     */
    @Transactional
    public LoginResponse login(OAuthLoginRequest oAuthLoginRequest) {
        OAuthLoginInfo authLoginInfo = oAuthLoginRequest.toDto();
        Provider provider = authLoginInfo.provider();

        // 인가 코드를 이용해 토큰 발급 요청
        String accessToken = providerFactory.getAccessToken(
                provider,
                authLoginInfo.propertyMap().get(AuthConstants.REDIRECT_URI),
                authLoginInfo.propertyMap().get(AuthConstants.CODE));

        // 토큰을 이용해 사용자 정보 가져오기
        OAuthUser oAuthUser = providerFactory.getOAuthUser(provider, accessToken);

        // 회원가입 or 로그인
        String email = oAuthUser.email();
        validateNotWithdrawnMember(email);

        boolean isNewMember = !memberReader.existsByEmailAndProvider(email, provider);
        Optional<Member> memberOpt = isNewMember ? Optional.empty() : Optional.of(memberReader.getByEmail(email));

        Token token = memberOpt.map(member -> {
            String generatedToken = jwtTokenProvider.createAccessToken(member);
            return new Token(generatedToken, jwtTokenProvider.getExpirationByToken(generatedToken));
        }).orElse(new Token(null, null));

        return new LoginResponse(
                token,
                isNewMember,
                new OAuthUserInfo(
                        memberOpt.map(Member::getId).orElse(null),
                        email,
                        oAuthUser.id(),
                        provider));
    }

    /**
     * 회원 가입
     */
    @Transactional
    public SignUpMemberResponse signUp(SignUpMemberRequest signUpRequest) {
        validateNickname(signUpRequest.nickname());
        validateEmail(signUpRequest.email());

        Member member = Member.create(signUpRequest);
        memberWriter.store(member);

        // 선호전통주 주종 등록
        memberUtils.processAlcoholTypes(member, signUpRequest);

        // 약관 등록
        memberUtils.processTermsAgreements(member, signUpRequest);

        String token = jwtTokenProvider.createAccessToken(member);

        return new SignUpMemberResponse(
                member.getId(),
                new Token(token, jwtTokenProvider.getExpirationByToken(token)));
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void deleteAccount(Member loginMember, WithdrawalRequest request) {
        loginMember.deleteAccount();
        withdrawalRecordWriter.store(
                WithdrawalRecord.create(request.withdrawalReason(), loginMember.getEmail(), loginMember.getNickname()));
    }

    /**
     * 닉네임 중복 확인
     */
    @Transactional(readOnly = true)
    public boolean checkNickname(String nickname) {
        return memberReader.existActiveNickname(nickname);
    }

    // ===== 토큰 관련 메서드 =====

    /**
     * 액세스 토큰 및 리프레시 토큰 갱신
     */
    @Transactional
    public RefreshResponse refresh(String refreshTokenCookie, HttpServletRequest request,
            HttpServletResponse response) {
        // 한 번의 호출로 Member와 RefreshToken을 함께 가져오도록 최적화
        Member member = jwtTokenProvider.getMemberFromToken(refreshTokenCookie);
        RefreshToken oldToken = validateAndGetOldToken(refreshTokenCookie);

        String ipAddress = httpRequestUtil.extractIpAddress(request);
        String userAgent = httpRequestUtil.extractUserAgent(request);

        // 토큰 환경 검증 및 토큰 회전
        validateTokenEnvironment(oldToken, ipAddress, userAgent, member);
        createAndSaveRefreshToken(member.getId(), oldToken.getId(), ipAddress, userAgent, response);

        // 새 액세스 토큰 생성 및 반환
        return new RefreshResponse(jwtTokenProvider.createAccessToken(member));
    }

    /**
     * 리프레시 토큰 등록
     */
    @Transactional
    public void registerRefreshToken(Long memberId, HttpServletRequest request, HttpServletResponse response) {
        String ipAddress = httpRequestUtil.extractIpAddress(request);
        String userAgent = httpRequestUtil.extractUserAgent(request);

        createAndSaveRefreshToken(memberId, null, ipAddress, userAgent, response);
    }

    /**
     * 로그아웃 처리 - 토큰 비활성화
     */
    @Transactional
    public void logout(String refreshTokenCookie) {
        refreshTokenRepository.findByToken(refreshTokenCookie)
                .ifPresent(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });
    }

    /**
     * 새 리프레시 토큰 생성 및 저장
     */
    public void createAndSaveRefreshToken(Long memberId, Long parentTokenId, String ipAddress, String userAgent,
            HttpServletResponse response) {

        String token = jwtTokenProvider.createRefreshToken(memberId);

        RefreshToken newToken = RefreshToken.builder()
                .token(token)
                .memberId(memberId)
                .parentTokenId(parentTokenId)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .expiresAt(LocalDateTime.now().plusSeconds(REFRESH_TOKEN_DURATION.getSeconds()))
                .build();

        refreshTokenRepository.save(newToken);
        setCookie(response, newToken.getToken());
    }

    /**
     * 리프레시 토큰 검증 및 조회
     */
    private RefreshToken validateAndGetOldToken(String tokenStr) {
        return refreshTokenRepository.findByToken(tokenStr)
                .orElseThrow(() -> new BaseException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
    }

    /**
     * 토큰 환경 검증 (IP, UserAgent 등)
     */
    private void validateTokenEnvironment(RefreshToken oldToken, String ipAddress, String userAgent, Member member) {
        // 환경 일치 여부 확인
        if (!oldToken.getIpAddress().equals(ipAddress) && !oldToken.getUserAgent().equals(userAgent)) {
            revokeAndThrow(oldToken,
                    String.format("의심스러운 활동 감지: IP=%s UA=%s Expected IP=%s UA=%s",
                            ipAddress, userAgent, oldToken.getIpAddress(), oldToken.getUserAgent()),
                    ErrorCode.REFRESH_TOKEN_INVALID);
        }

        // 토큰이 이미 비활성화되었는지 확인
        if (oldToken.isRevoked()) {
            if (refreshTokenRepository.existsByParentTokenId(oldToken.getId())) {
                refreshTokenRepository.deleteByMemberId(member.getId());
                throw new BaseException(
                        String.format("리프레시 토큰 재사용 감지: IP=%s User-Agent=%s",
                                ipAddress, userAgent),
                        ErrorCode.REFRESH_TOKEN_INVALID);
            }
            throw new BaseException("이미 회전된 토큰", ErrorCode.REFRESH_TOKEN_ALREADY_ROTATED);
        }

        // 토큰 비활성화
        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);
    }

    /**
     * 토큰 비활성화 및 예외 발생
     */
    private void revokeAndThrow(RefreshToken oldToken, String message, ErrorCode errorCode) {
        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);
        throw new BaseException(message, errorCode);
    }

    /**
     * 리프레시 토큰 쿠키 설정
     */
    private void setCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_HEADER_NAME, token);
        cookie.setMaxAge((int) REFRESH_TOKEN_DURATION.getSeconds());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    // ===== 유효성 검증 메서드 =====

    private void validateNickname(String nickname) {
        if (memberReader.existActiveNickname(nickname)) {
            throw new InvalidParamException(ErrorCode.MEMBER_NICKNAME_DUPLICATE);
        }
    }

    private void validateNotWithdrawnMember(String email) {
        if (withdrawalRecordReader.existEmail(email)) {
            throw new InvalidParamException(ErrorCode.MEMBER_WITHDRAWN);
        }
    }

    private void validateEmail(String email) {
        if (memberReader.existActiveEmail(email)) {
            throw new InvalidParamException(ErrorCode.MEMBER_EMAIL_DUPLICATE);
        }
    }

}