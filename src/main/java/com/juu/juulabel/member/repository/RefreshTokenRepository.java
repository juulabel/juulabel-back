package com.juu.juulabel.member.repository;

import java.util.Optional;

import com.juu.juulabel.member.domain.RefreshToken;

/**
 * Interface for refresh token persistence operations.
 * This abstraction allows swapping implementations (e.g., JPA, Redis).
 */
public interface RefreshTokenRepository {

    /**
     * Finds a refresh token by its token string.
     *
     * @param token The token string.
     * @return An Optional containing the RefreshToken if found, otherwise empty.
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Checks if a refresh token exists with the given parent token ID.
     * Used for detecting token reuse after rotation.
     *
     * @param parentTokenId The ID of the parent token.
     * @return true if a token with the specified parent ID exists, false otherwise.
     */
    boolean existsByParentTokenId(Long parentTokenId);

    /**
     * Deletes all refresh tokens associated with a specific member ID.
     * Used when revoking all tokens for a user due to security concerns (e.g.,
     * reuse detection).
     *
     * @param memberId The ID of the member whose tokens should be deleted.
     */
    void deleteByMemberId(Long memberId);

    /**
     * Saves a refresh token entity.
     * Used for storing new tokens during issuance or rotation.
     *
     * @param refreshToken The RefreshToken entity to save.
     * @return The saved RefreshToken entity.
     */
    RefreshToken save(RefreshToken refreshToken);

    /**
     * Deletes a specific refresh token.
     * (Optional: Might be useful for explicit deletion scenarios if needed later)
     *
     * @param refreshToken The RefreshToken entity to delete.
     */

}