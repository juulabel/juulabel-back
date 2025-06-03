package com.juu.juulabel.auth.repository;

import com.juu.juulabel.member.token.UserSession;

import org.springframework.data.repository.CrudRepository;

public interface UserSessionRepository extends CrudRepository<UserSession, String> {

    void deleteAllByMemberId(Long memberId);
}