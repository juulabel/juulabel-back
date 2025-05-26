package com.juu.juulabel.member.service;

import com.juu.juulabel.common.dto.request.*;
import com.juu.juulabel.common.dto.response.*;
import com.juu.juulabel.dailylife.response.DailyLifeListRequest;
import com.juu.juulabel.member.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 회원 서비스 파사드 (Facade) 클래스
 * 다른 회원 관련 서비스 클래스들에 위임하는 역할을 담당
 */
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberProfileService memberProfileService;
    private final MemberLookupService memberLookupService;
    private final MemberContentService memberContentService;

    /**
     * 닉네임 중복 확인
     */
    @Transactional(readOnly = true)
    public boolean checkNickname(String nickname) {
        return memberProfileService.checkNickname(nickname);
    }

    /**
     * 프로필 수정
     */
    @Transactional
    public UpdateProfileResponse updateProfile(Member loginMember, UpdateProfileRequest request, MultipartFile image) {
        return memberProfileService.updateProfile(loginMember, request, image);
    }

    /**
     * 내 공간 정보 조회
     */
    @Transactional(readOnly = true)
    public MySpaceResponse getMySpace(Member loginMember) {
        return memberProfileService.getMySpace(loginMember);
    }

    /**
     * 내 정보 조회
     */
    @Transactional(readOnly = true)
    public MyInfoResponse getMyInfo(Member loginMember) {
        return memberProfileService.getMyInfo(loginMember);
    }

    /**
     * 타 유저 프로필 조회
     */
    @Transactional(readOnly = true)
    public MemberProfileResponse getMemberProfile(Member loginMember, Long memberId) {
        return memberProfileService.getMemberProfile(loginMember, memberId);
    }

    /**
     * ID로 회원 조회
     */
    @Transactional(readOnly = true)
    public Member findById(Long memberId) {
        return memberLookupService.findById(memberId);
    }

    /**
     * 이메일로 회원 조회
     */
    @Transactional(readOnly = true)
    public Member getMemberByEmail(String email) {
        return memberLookupService.getMemberByEmail(email);
    }

    /**
     * 내가 작성한 일상생활 목록 조회
     */
    @Transactional(readOnly = true)
    public MyDailyLifeListResponse loadMyDailyLifeList(Member member, DailyLifeListRequest request) {
        return memberContentService.loadMyDailyLifeList(member, request);
    }

    /**
     * 특정 회원이 작성한 일상생활 목록 조회
     */
    @Transactional(readOnly = true)
    public DailyLifeListResponse loadMemberDailyLifeList(Member loginMember, DailyLifeListRequest request,
            Long memberId) {
        return memberContentService.loadMemberDailyLifeList(loginMember, request, memberId);
    }

    /**
     * 내가 작성한 시음노트 목록 조회
     */
    @Transactional(readOnly = true)
    public MyTastingNoteListResponse loadMyTastingNoteList(Member member, TastingNoteListRequest request) {
        return memberContentService.loadMyTastingNoteList(member, request);
    }

    /**
     * 특정 회원이 작성한 시음노트 목록 조회
     */
    @Transactional(readOnly = true)
    public TastingNoteListResponse loadMemberTastingNoteList(Member loginMember, TastingNoteListRequest request,
            Long memberId) {
        return memberContentService.loadMemberTastingNoteList(loginMember, request, memberId);
    }

    /**
     * 전통주 저장하기 또는 저장 취소
     * 
     * @return true if saved, false if unsaved
     */
    @Transactional
    public boolean saveAlcoholicDrinks(Member member, Long alcoholicDrinksId) {
        return memberContentService.saveAlcoholicDrinks(member, alcoholicDrinksId);
    }

    /**
     * 내가 저장한 전통주 목록 조회
     */
    @Transactional(readOnly = true)
    public MyAlcoholicDrinksListResponse loadMyAlcoholicDrinks(Member member, MyAlcoholicDrinksListRequest request) {
        return memberContentService.loadMyAlcoholicDrinks(member, request);
    }
}
