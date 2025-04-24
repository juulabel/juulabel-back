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
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생하였습니다."),
    S3_UPLOADER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3 업로드 중 오류가 발생하였습니다."),

    /**
     * Common
     */
    IS_NULL(HttpStatus.BAD_REQUEST, "NULL 값이 들어왔습니다."),
    COMMON_INVALID_PARAM(HttpStatus.BAD_REQUEST, "요청한 값이 올바르지 않습니다."),
    INVALID_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "인증이 올바르지 않습니다."),

    /**
     * Json Web Token
     */
    JWT_EXPIRED_EXCEPTION(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    JWT_UNSUPPORTED_EXCEPTION(HttpStatus.BAD_REQUEST, "지원되지 않는 토큰입니다."),
    JWT_MALFORMED_EXCEPTION(HttpStatus.UNAUTHORIZED, "잘못된 형식의 토큰입니다."),
    JWT_ILLEGAL_ARGUMENT_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 인자가 전달되었습니다."),

    /**
     * Authentication
     */
    OAUTH_PROVIDER_NOT_FOUND(HttpStatus.BAD_REQUEST, "Provider를 찾을 수 없습니다."),

    /**
     * Admin, Member
     */
    MEMBER_WITHDRAWN(HttpStatus.BAD_REQUEST, "탈퇴한 회원입니다."),
    ADMIN_NOT_FOUND(HttpStatus.BAD_REQUEST, "어드민 정보를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "회원 정보를 찾을 수 없습니다."),
    MEMBER_EMAIL_DUPLICATE(HttpStatus.BAD_REQUEST, "중복된 이메일입니다."),
    MEMBER_NICKNAME_DUPLICATE(HttpStatus.BAD_REQUEST, "중복된 닉네임입니다."),

    /**
     * TERMS
     */
    TERMS_NOT_FOUND(HttpStatus.BAD_REQUEST, "약관 정보를 찾을 수 없습니다."),
    TERMS_AGREEMENT_MISMATCH(HttpStatus.BAD_REQUEST, "사용중인 약관과 요청한 약관 동의 리스트가 다릅니다."),
    TERMS_AGREEMENT_MISSING_REQUIRED(HttpStatus.BAD_REQUEST, "필수 약관에 동의하지 않았습니다."),

    /**
     * Alcohol
     */
    ALCOHOL_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "주종을 찾을 수 없습니다."),
    ALCOHOLIC_DRINKS_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "전통주를 찾을 수 없습니다."),
    ALCOHOLIC_DRINKS_INVALID_RATING(HttpStatus.BAD_REQUEST, "잘못된 평점입니다. 평점은 0.00에서 5.00 사이여야 합니다."),


    /**
     * Notification
     */
    NOTIFICATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 알림이 존재하지 않거나, 권한이 없습니다."),


    /**
     * Comment
     */
    COMMENT_NOT_WRITER(HttpStatus.BAD_REQUEST, "댓글 작성자가 아닙니다."),
    COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "댓글을 찾을 수 없습니다."),

    /**
     * DailyLife
     */
    DAILY_LIFE_NOT_FOUND(HttpStatus.BAD_REQUEST, "일상생활 게시글을 찾을 수 없습니다."),
    DAILY_LIFE_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "이미 삭제된 일상생활 게시글입니다."),
    DAILY_LIFE_NOT_WRITER(HttpStatus.BAD_REQUEST, "게시글 작성자가 아닙니다."),

    /**
     * FILE
     */
    EXCEEDED_FILE_COUNT(HttpStatus.BAD_REQUEST, "파일 첨부 허용 개수를 초과했습니다."),
    FILE_TOO_LARGE(HttpStatus.BAD_REQUEST, "파일 크기가 10MB를 초과했습니다."),


    /**
     * Tasting Note
     */
    INVALID_ALCOHOL_TYPE_COLOR(HttpStatus.BAD_REQUEST, "주종에 연결된 시각 정보가 없습니다."),
    INVALID_ALCOHOL_TYPE_SCENT(HttpStatus.BAD_REQUEST, "주종에 연결된 후각 정보가 없습니다."),
    INVALID_ALCOHOL_TYPE_SENSORY(HttpStatus.BAD_REQUEST, "주종에 연결된 촉각 정보가 없습니다."),
    INVALID_ALCOHOL_TYPE_FLAVOR(HttpStatus.BAD_REQUEST, "주종에 연결된 미각 정보가 없습니다."),

    COLOR_NOT_FOUND(HttpStatus.BAD_REQUEST, "색상을 찾을 수 없습니다"),

    TASTING_NOTE_NOT_FOUND(HttpStatus.BAD_REQUEST, "시음노트 게시글을 찾을 수 없습니다."),
    TASTING_NOTE_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "이미 삭제된 시음노트 게시글입니다."),
    TASTING_NOTE_NOT_WRITER(HttpStatus.BAD_REQUEST, "게시글 작성자가 아닙니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

}
