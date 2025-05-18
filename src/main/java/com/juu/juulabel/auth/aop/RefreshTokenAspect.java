package com.juu.juulabel.auth.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.juu.juulabel.auth.service.TokenService;
import com.juu.juulabel.common.dto.response.LoginResponse;
import com.juu.juulabel.common.dto.response.SignUpMemberResponse;
import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.response.CommonResponse;
import com.juu.juulabel.member.domain.Member;

import java.util.Arrays;
import java.util.Optional;
import java.util.Map;
import java.util.function.Function;
import java.util.HashMap;

@Aspect
@Component
public class RefreshTokenAspect {
    private final TokenService tokenService;
    private final SpelExpressionParser parser;

    private static final Map<Class<?>, Function<Object, Long>> memberIdExtractors = new HashMap<>();

    static {
        memberIdExtractors.put(LoginResponse.class, result -> ((LoginResponse) result).oAuthUserInfo().memberId());
        memberIdExtractors.put(SignUpMemberResponse.class, result -> ((SignUpMemberResponse) result).memberId());
    }

    public RefreshTokenAspect(TokenService tokenService) {
        this.tokenService = tokenService;
        this.parser = new SpelExpressionParser();
    }

    @AfterReturning(pointcut = "@annotation(setRefreshTokenCookie)", returning = "responseEntity")
    public void setRefreshTokenCookie(JoinPoint joinPoint,
            SetRefreshTokenCookie setRefreshTokenCookie,
            ResponseEntity<CommonResponse<?>> responseEntity) {

        boolean isNewSession = setRefreshTokenCookie.isNewSession();
        String parentTokenId = setRefreshTokenCookie.parentTokenId();

        Optional<Long> memberId = extractMemberId(isNewSession, joinPoint, responseEntity);

        if (memberId.isEmpty()) {
            if (!isNewSession) {
                // 토큰 리프레시 멤버 추출 실패
                throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
            // 비회원 유저가 로그인 했을 때 쿠키 설정 안함
            return;
        }

        String refreshToken = extractRefreshTokenCookie(joinPoint, parentTokenId);
        tokenService.saveAndSetCookie(memberId.get(), refreshToken);
    }

    private String extractRefreshTokenCookie(JoinPoint joinPoint, String parentTokenId) {
        if (parentTokenId.isEmpty()) {
            return null;
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        StandardEvaluationContext context = new StandardEvaluationContext();

        // Bind method arguments
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }

        Object evaluated = parser.parseExpression(parentTokenId).getValue(context);
        if (!(evaluated instanceof String)) {
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return (String) evaluated;
    }

    private Optional<Long> extractMemberId(boolean isNewSession, JoinPoint joinPoint,
            ResponseEntity<CommonResponse<?>> responseEntity) {
        // For existing sessions, extract from Member object
        if (!isNewSession) {
            return findFirstArgOfType(joinPoint, Member.class)
                    .map(Member::getId);
        }

        // For new sessions, extract from response body
        CommonResponse<?> body = responseEntity.getBody();
        if (body == null || body.result() == null) {
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        Object result = body.result();
        Class<?> resultClass = result.getClass();

        Function<Object, Long> extractor = memberIdExtractors.get(resultClass);
        if (extractor != null) {
            return Optional.of(extractor.apply(result));
        }

        throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    private <T> Optional<T> findFirstArgOfType(JoinPoint joinPoint, Class<T> clazz) {
        return Arrays.stream(joinPoint.getArgs())
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .findFirst();
    }
}