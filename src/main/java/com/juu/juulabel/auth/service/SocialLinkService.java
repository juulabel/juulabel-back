package com.juu.juulabel.auth.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.juu.juulabel.auth.domain.SignUpToken;
import com.juu.juulabel.auth.domain.SocialLink;
import com.juu.juulabel.auth.repository.SocialLinkRepository;
import com.juu.juulabel.common.util.HashingUtil;
import com.juu.juulabel.common.util.HttpRequestUtil;
import com.juu.juulabel.common.util.IpAddressExtractor;
import com.juu.juulabel.member.request.OAuthUser;
import com.juu.juulabel.common.exception.AuthException;
import com.juu.juulabel.common.exception.code.ErrorCode;

@Service
@RequiredArgsConstructor
public class SocialLinkService {

    private final SocialLinkRepository socialLinkRepository;

    public void save(OAuthUser oAuthUser, String nonce) {
        SocialLink socialLink = SocialLink.builder()
                .hashedEmail(HashingUtil.hashSha256(oAuthUser.email()))
                .provider(oAuthUser.provider())
                .providerId(oAuthUser.id())
                .deviceId(HttpRequestUtil.getDeviceId())
                .userAgent(HttpRequestUtil.getUserAgent())
                .ipAddress(IpAddressExtractor.getClientIpAddress())
                .nonce(nonce)
                .build();
        socialLinkRepository.save(socialLink);
    }

    public void verify(SignUpToken signUpToken) {
        String hashedEmail = HashingUtil.hashSha256(signUpToken.email());

        SocialLink socialLink = socialLinkRepository.findById(hashedEmail)
                .orElseThrow(() -> new AuthException(ErrorCode.SOCIAL_LINK_NOT_FOUND));

        socialLink.validate(signUpToken);

        socialLink.markAsUsed();
        socialLinkRepository.save(socialLink);
    }
}
