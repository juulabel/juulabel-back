package com.juu.juulabel.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * Server
     */
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "유효성 검사 실패"),
    CONSTRAINT_VIOLATION(HttpStatus.CONFLICT, "제약 조건 위반"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생하였습니다."),
    S3_UPLOADER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3 업로드 중 오류가 발생하였습니다."),

    /**
     * Common
     */
    NOT_FOUND_ENUM_CONSTANT(HttpStatus.BAD_REQUEST, "열거형 상수값을 찾을 수 없습니다."),
    IS_NULL(HttpStatus.BAD_REQUEST, "NULL 값이 들어왔습니다."),
    COMMON_INVALID_PARAM(HttpStatus.BAD_REQUEST, "요청한 값이 올바르지 않습니다."),
    INVALID_AUTHENTICATION(HttpStatus.BAD_REQUEST, "인증이 올바르지 않습니다."),
    NO_SUCH_METHOD(HttpStatus.BAD_REQUEST, "메소드를 찾을 수 없습니다."),

    /**
     * Json Web Token
     */
    EXPIRED_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    UNSUPPORTED_JWT_EXCEPTION(HttpStatus.BAD_REQUEST, "지원되지 않는 토큰입니다."),
    MALFORMED_JWT_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 형식의 토큰입니다."),
    SIGNATURE_EXCEPTION(HttpStatus.BAD_REQUEST, "토큰 서명이 올바르지 않습니다."),
    ILLEGAL_ARGUMENT_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 인자가 전달되었습니다."),

    /**
     * Authentication
     */
    INTERNAL_AUTHENTICATION_SERVICE(HttpStatus.BAD_REQUEST, "인증 서비스가 존재하지 않습니다."),
    NOT_FOUND_OAUTH_PROVIDER(HttpStatus.BAD_REQUEST, "PROVIDER를 찾을 수 없습니다."),
    NON_EXPIRED_ACCOUNT(HttpStatus.BAD_REQUEST, "사용자 계정이 탈퇴되었습니다."),
    NON_LOCKED_ACCOUNT(HttpStatus.BAD_REQUEST, "사용자 계정이 정지되었습니다."),
    DISABLE_ACCOUNT(HttpStatus.BAD_REQUEST, "사용자 계정은 비활성화 상태입니다."),
    EXPIRED_CREDENTIAL(HttpStatus.BAD_REQUEST, "사용자 인증 정보가 만료되었습니다."),

    /**
     * SMS
     */
    INVALID_OR_EXPIRED_SMS(HttpStatus.BAD_REQUEST, "SMS 인증 시간이 만료되었거나 인증을 받지 않았습니다."),
    INVALID_SMS_CODE(HttpStatus.BAD_REQUEST, "잘못된 SMS 코드입니다."),
    MAX_VERIFY_ATTEMPTS(HttpStatus.BAD_REQUEST, "SMS 인증 시도에 실패했습니다."),

    /**
     * Admin, Member
     */
    NOT_FOUND_ADMIN(HttpStatus.BAD_REQUEST, "어드민 정보를 찾을 수 없습니다."),
    NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "회원 정보를 찾을 수 없습니다."),
    EXIST_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일입니다."),
    EXIST_PHONE(HttpStatus.BAD_REQUEST, "중복된 전화번호입니다."),
    EXIST_NICKNAME(HttpStatus.BAD_REQUEST, "중복된 닉네임입니다."),
    FAIL_LOGIN(HttpStatus.BAD_REQUEST, "이메일 또는 비밀번호가 잘못되었습니다."),
    NOT_MATCH_CONFIRM(HttpStatus.BAD_REQUEST, "입력 값과 확인 값이 일치하지 않습니다."),
    MATCH_PASSWORD_AND_NEW_PASSWORD(HttpStatus.BAD_REQUEST, "현재 비밀번호와 변경할 비밀번호가 일치합니다."),
    NOT_FOUND_BLOCK(HttpStatus.BAD_REQUEST, "회원 차단 정보를 찾을 수 없습니다."),
    CAN_NOT_BLOCK(HttpStatus.BAD_REQUEST, "해당 계정은 차단할 수 없습니다."),
    ALREADY_BLOCK(HttpStatus.BAD_REQUEST, "해당 계정은 이미 차단되어 있습니다."),
    ALREADY_UNBLOCK(HttpStatus.BAD_REQUEST, "해당 계정은 차단되어 있지 않습니다."),

    /**
     * TERMS
     */
    NOT_FOUND_TERMS(HttpStatus.BAD_REQUEST, "약관 정보를 찾을 수 없습니다."),
    MISMATCH_TERMS_AGREEMENT(HttpStatus.BAD_REQUEST, "사용중인 약관과 요청한 약관 동의 리스트가 다릅니다."),
    MISSING_REQUIRED_AGREEMENT(HttpStatus.BAD_REQUEST, "필수 약관에 동의하지 않았습니다."),

    /**
     * Alcohol
     */
    NOT_FOUND_ALCOHOL_TYPE(HttpStatus.BAD_REQUEST, "주종을 찾을 수 없습니다."),

    /**
     * Category
     */
    NOT_FOUND_CATEGORY(HttpStatus.BAD_REQUEST, "카테고리를 찾을 수 없습니다."),

    /**
     * File
     */
    NOT_FOUND_FILE(HttpStatus.BAD_REQUEST, "파일을 찾을 수 없습니다"),
    INVALID_FILE_NAME_OR_EXTENSIONS(HttpStatus.BAD_REQUEST, "파일 이름 또는 확장자가 잘못되었습니다."),
    INVALID_EXTENSIONS(HttpStatus.BAD_REQUEST, "파일의 확장자가 올바르지 않습니다."),
    EXCEEDED_FILE_SIZE(HttpStatus.BAD_REQUEST, "파일의 허용 용량을 초과했습니다."),
    EXCEEDED_FILE_COUNT(HttpStatus.BAD_REQUEST, "파일 첨부 허용 개수를 초과했습니다."),

    /**
     * Search
     */
    NOT_FOUND_SEARCH_TYPE(HttpStatus.BAD_REQUEST, "검색 타입을 찾을 수 없습니다"),

    /**
     * Tasting Note
     */
    INVALID_RATING_VALUE(HttpStatus.BAD_REQUEST, "평점은 0에서 5 사이여야 하며, 0.25 단위로 증가해야 합니다."),
    INVALID_SENSORY_TYPE(HttpStatus.BAD_REQUEST, "촉각 정보를 찾을 수 없습니다."),
    INVALID_FLAVOR_TYPE(HttpStatus.BAD_REQUEST, "미각 정보를 찾을 수 없습니다."),
    INVALID_SENSORY_LEVEL(HttpStatus.BAD_REQUEST, "촉각 점수를 찾을 수 없습니다."),
    INVALID_FLAVOR_LEVEL(HttpStatus.BAD_REQUEST, "미각 점수를 찾을 수 없습니다."),
    TASTING_NOTE_MAX_IMAGE_COUNT_EXCEEDED(HttpStatus.BAD_REQUEST, "이미지는 최대 9장까지 추가할 수 있습니다"),


    ;

    private final HttpStatus httpStatus;
    private final String message;

}
