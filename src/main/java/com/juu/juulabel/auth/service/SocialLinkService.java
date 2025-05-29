package com.juu.juulabel.auth.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.juu.juulabel.auth.domain.SocialLink;
import com.juu.juulabel.auth.repository.SocialLinkRepository;
import com.juu.juulabel.common.util.DeviceIdExtractor;
import com.juu.juulabel.common.util.HashingUtil;
import com.juu.juulabel.common.util.IpAddressExtractor;
import com.juu.juulabel.common.util.UserAgentExtractor;
import com.juu.juulabel.member.domain.Provider;
import com.juu.juulabel.common.exception.AuthException;
import com.juu.juulabel.common.exception.code.ErrorCode;

@Service
@RequiredArgsConstructor
public class SocialLinkService {

    private final SocialLinkRepository socialLinkRepository;

    public void save(String email, Provider provider, String providerId) {
        SocialLink socialLink = SocialLink.builder()
                .hashedEmail(HashingUtil.hashSha256(email))
                .provider(provider)
                .providerId(providerId)
                .deviceId(DeviceIdExtractor.getDeviceId())
                .userAgent(UserAgentExtractor.getUserAgent())
                .ipAddress(IpAddressExtractor.getClientIpAddress())
                .build();
        socialLinkRepository.save(socialLink);
    }

    public void verify(String email, Provider provider, String providerId) {
        String hashedEmail = HashingUtil.hashSha256(email);

        SocialLink socialLink = socialLinkRepository.findById(hashedEmail)
                .orElseThrow(() -> new AuthException(ErrorCode.SOCIAL_LINK_NOT_FOUND));

        socialLink.validate(provider, providerId, DeviceIdExtractor.getDeviceId(),
                UserAgentExtractor.getUserAgent());

        socialLink.markAsUsed();
        socialLinkRepository.save(socialLink);
    }
}
