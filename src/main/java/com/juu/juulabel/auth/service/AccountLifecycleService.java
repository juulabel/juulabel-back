package com.juu.juulabel.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.juu.juulabel.common.dto.request.WithdrawalRequest;
import com.juu.juulabel.common.exception.AuthException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.MemberStatus;
import com.juu.juulabel.member.domain.WithdrawalRecord;
import com.juu.juulabel.member.repository.WithdrawalRecordWriter;
import com.juu.juulabel.redis.UserSessionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service dedicated to account lifecycle operations.
 * Handles logout, account deletion, and session cleanup.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountLifecycleService {

    private final UserSessionManager sessionManager;
    private final WithdrawalRecordWriter withdrawalRecordWriter;

    /**
     * Logs out current user by invalidating their session
     */
    public void logout() {
        try {
            sessionManager.invalidateSession();
            log.debug("User logout successful");
        } catch (Exception e) {
            log.warn("Error during logout: {}", e.getMessage());
            // Don't throw exception for logout failures
        }
    }

    /**
     * Permanently deletes member account and creates audit record
     * @param member Authenticated member requesting deletion
     * @param request Withdrawal request with reason
     */
    @Transactional
    public void deleteAccount(Member member, WithdrawalRequest request) {
        // Validate member can be deleted
        if (member.getStatus() == MemberStatus.WITHDRAWAL) {
            throw new AuthException("Member already withdrawn", ErrorCode.MEMBER_WITHDRAWN);
        }

        // Mark member as deleted (soft delete)
        member.deleteAccount();

        // Create audit record
        WithdrawalRecord withdrawalRecord = WithdrawalRecord.create(
                request.withdrawalReason(),
                member.getEmail(),
                member.getNickname());
        withdrawalRecordWriter.store(withdrawalRecord);

        // Revoke all sessions
        sessionManager.invalidateAllUserSessions(member.getId());

        log.debug("Account deletion completed for member: {}", member.getEmail());
    }
} 