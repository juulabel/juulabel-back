package com.juu.juulabel.common.provider.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.juu.juulabel.common.constants.AuthConstants;
import com.juu.juulabel.common.util.HashingUtil;
import com.juu.juulabel.common.util.HttpRequestUtil;
import com.juu.juulabel.common.util.IpAddressExtractor;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.auth.domain.ClientId;
import com.juu.juulabel.auth.domain.RefreshToken;

@Component
public class RefreshTokenProvider extends MemberTokenProvider {

    public RefreshTokenProvider(@Value("${spring.jwt.refresh-key}") String secretKey) {
        super(secretKey);
    }

    public RefreshToken buildRefreshToken(Member member) {
        String token = createToken(member, AuthConstants.REFRESH_TOKEN_DURATION);
        String hashedToken = HashingUtil.hashSha256(token);

        return RefreshToken.builder()
                .token(token)
                .hashedToken(hashedToken)
                .memberId(member.getId())
                .clientId(ClientId.WEB)
                .deviceId(HttpRequestUtil.getDeviceId())
                .ipAddress(IpAddressExtractor.getClientIpAddress())
                .userAgent(HttpRequestUtil.getUserAgent())
                .build();
    }
}
