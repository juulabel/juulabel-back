package com.juu.juulabel.common.converter;

import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.member.domain.Provider;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ProviderConverter implements Converter<String, Provider> {

    private static final Set<Provider> ALLOWED_PROVIDERS = Set.of(Provider.GOOGLE, Provider.KAKAO, Provider.APPLE);

    @Override
    public Provider convert(String source) {
        try {
            final Provider provider = Provider.valueOf(source.toUpperCase());
            if (!ALLOWED_PROVIDERS.contains(provider)) {
                throw new BaseException(ErrorCode.OAUTH_PROVIDER_NOT_FOUND);
            }
            return provider;
        } catch (IllegalArgumentException e) {
            throw new BaseException(ErrorCode.OAUTH_PROVIDER_NOT_FOUND);
        }
    }
}