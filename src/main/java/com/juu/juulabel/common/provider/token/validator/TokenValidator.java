package com.juu.juulabel.common.provider.token.validator;

/**
 * Interface for validating token claims against business rules
 */
public interface TokenValidator<T> {

    /**
     * Validates the token claims
     * 
     * @param claims the parsed token claims
     * @throws RuntimeException if validation fails
     */
    void validate(T claims);

    /**
     * Returns the validation type for logging/debugging
     */
    String getValidationType();
}