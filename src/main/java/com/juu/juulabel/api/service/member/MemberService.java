package com.juu.juulabel.api.service.member;

import com.juu.juulabel.api.factory.OAuthProviderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final OAuthProviderFactory providerFactory;

}
