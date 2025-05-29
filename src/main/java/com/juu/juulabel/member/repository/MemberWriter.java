package com.juu.juulabel.member.repository;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.annotation.Writer;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.repository.jpa.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Writer
@RequiredArgsConstructor
public class MemberWriter {

    private final MemberJpaRepository memberJpaRepository;

    /**
     * Stores a member entity to the database.
     * Handles SQL constraint violations and converts them to appropriate business
     * exceptions.
     *
     * @param member the member entity to store
     * @throws InvalidParamException if nickname or email constraint violation
     *                               occurs
     */
    public void store(Member member) {
        try {
            memberJpaRepository.save(member);
        } catch (DataIntegrityViolationException e) {
            handleConstraintViolation(e);
        }
    }

    /**
     * Handles database constraint violations and converts them to business
     * exceptions.
     * 
     * @param e the DataIntegrityViolationException to handle
     * @throws InvalidParamException for known constraint violations
     */
    private void handleConstraintViolation(DataIntegrityViolationException e) {
        String errorMessage = getRootCauseMessage(e);

        if (errorMessage == null || errorMessage.isEmpty()) {
            throw new InvalidParamException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        if (isNicknameConstraintViolation(errorMessage)) {
            throw new InvalidParamException(ErrorCode.MEMBER_NICKNAME_DUPLICATE);
        }

        if (isEmailConstraintViolation(errorMessage)) {
            throw new InvalidParamException(ErrorCode.MEMBER_EMAIL_DUPLICATE);
        }

        // Re-throw the original exception if it's not a recognized constraint violation
        throw new InvalidParamException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    /**
     * Extracts the root cause error message from the exception chain.
     */
    private String getRootCauseMessage(DataIntegrityViolationException e) {
        Throwable rootCause = e;
        while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }
        return rootCause.getMessage();
    }

    /**
     * Checks if the error message indicates a nickname constraint violation.
     */
    private boolean isNicknameConstraintViolation(String errorMessage) {
        String lowerCaseMessage = errorMessage.toLowerCase();
        return lowerCaseMessage.contains("unique_nickname") ||
                (lowerCaseMessage.contains("duplicate entry") && lowerCaseMessage.contains("unique_nickname"));
    }

    /**
     * Checks if the error message indicates an email constraint violation.
     */
    private boolean isEmailConstraintViolation(String errorMessage) {
        String lowerCaseMessage = errorMessage.toLowerCase();
        return lowerCaseMessage.contains("unique_email") ||
                lowerCaseMessage.contains("uk_") ||
                (lowerCaseMessage.contains("duplicate entry") &&
                        (lowerCaseMessage.contains("email") || lowerCaseMessage.contains("uk_")));
    }
}
