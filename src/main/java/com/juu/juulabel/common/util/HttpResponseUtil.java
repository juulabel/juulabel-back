package com.juu.juulabel.common.util;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.properties.RedirectProperties;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HttpResponseUtil extends AbstractHttpUtil {

    private final RedirectProperties redirectProperties;

    public void redirectToLogin() {
        redirect(redirectProperties.getLoginUrl());
    }

    public void redirectToSignup() {
        redirect(redirectProperties.getSignupUrl());
    }

    public void redirectToError() {
        redirect(redirectProperties.getErrorUrl());
    }

    private void redirect(String url) {
        try {
            HttpServletResponse response = getCurrentResponse();
            response.sendRedirect(url);
        } catch (IOException e) {
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

}
