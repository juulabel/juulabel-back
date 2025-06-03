package com.juu.juulabel.auth.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.juu.juulabel.common.dto.request.SignUpMemberRequest;
import com.juu.juulabel.common.exception.AuthException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.MemberStatus;
import com.juu.juulabel.member.repository.MemberWriter;
import com.juu.juulabel.member.request.OAuthUser;
import com.juu.juulabel.member.util.MemberUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service dedicated to member creation and signup operations.
 * Handles new member creation and signup completion.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberCreationService {

    private final MemberWriter memberWriter;
    private final MemberUtils memberUtils;

    /**
     * Creates a new pending member from OAuth user data
     * @param oAuthUser OAuth user information
     * @return Generated nonce for the new member
     */
    @Transactional
    public String createPendingMember(OAuthUser oAuthUser) {
        // Generate unique nonce for new member
        String nonce = UUID.randomUUID().toString();

        // Create new pending member
        Member newMember = Member.create(oAuthUser, nonce);
        memberWriter.store(newMember);

        log.debug("Created new pending member for email: {} with nonce: {}", 
                 oAuthUser.email(), nonce);
        
        return nonce;
    }

    /**
     * Completes member signup with additional information
     * @param member Pre-authenticated member from signup token
     * @param signUpRequest Additional member registration details
     * @return The completed member
     */
    @Transactional
    public Member completeSignup(Member member, SignUpMemberRequest signUpRequest) {
        // Validate member status
        if (member.getStatus() != MemberStatus.PENDING) {
            throw new AuthException("Member is not in pending status", ErrorCode.INVALID_AUTHENTICATION);
        }

        // Complete signup process
        member.completeSignUp(signUpRequest);
        memberWriter.store(member);

        // Process additional member data
        memberUtils.processMemberData(member, signUpRequest);

        log.debug("Completed signup for member: {}", member.getEmail());
        
        return member;
    }

    /**
     * Gets existing nonce for pending member (used for existing pending members)
     * @param member Existing pending member
     * @return The member's existing nonce
     */
    public String getExistingNonce(Member member) {
        if (member.getStatus() != MemberStatus.PENDING) {
            throw new AuthException("Member is not in pending status", ErrorCode.INVALID_AUTHENTICATION);
        }
        
        return member.getNickname(); // The nonce is stored in nickname for pending members
    }
} 