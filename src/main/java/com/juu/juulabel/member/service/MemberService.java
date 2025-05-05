package com.juu.juulabel.member.service;

import com.juu.juulabel.alcohol.domain.AlcoholicDrinks;
import com.juu.juulabel.alcohol.repository.AlcoholTypeReader;
import com.juu.juulabel.alcohol.repository.AlcoholicDrinksReader;
import com.juu.juulabel.alcohol.repository.TastingNoteReader;
import com.juu.juulabel.alcohol.response.AlcoholicDrinksSummary;
import com.juu.juulabel.common.dto.request.*;
import com.juu.juulabel.common.dto.response.*;
import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.dailylife.repository.DailyLifeReader;
import com.juu.juulabel.dailylife.response.DailyLifeListRequest;
import com.juu.juulabel.dailylife.response.DailyLifeSummary;
import com.juu.juulabel.dailylife.response.MyDailyLifeSummary;
import com.juu.juulabel.follow.repository.FollowReader;
import com.juu.juulabel.member.domain.*;
import com.juu.juulabel.member.repository.*;
import com.juu.juulabel.member.repository.jpa.MemberJpaRepository;
import com.juu.juulabel.member.util.MemberUtils;
import com.juu.juulabel.s3.S3Service;
import com.juu.juulabel.s3.UploadImageInfo;
import com.juu.juulabel.tastingnote.request.MyTastingNoteSummary;
import com.juu.juulabel.tastingnote.request.TastingNoteSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * 회원 프로필 및 계정 관리 서비스
 */
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberReader memberReader;
    private final MemberAlcoholTypeWriter memberAlcoholTypeWriter;
    private final MemberAlcoholTypeReader memberAlcoholTypeReader;
    private final AlcoholTypeReader alcoholTypeReader;
    private final S3Service s3Service;
    private final DailyLifeReader dailyLifeReader;
    private final TastingNoteReader tastingNoteReader;
    private final MemberJpaRepository memberJpaRepository;
    private final FollowReader followReader;

    private final MemberUtils memberUtils;
    private final AlcoholicDrinksReader alcoholicDrinksReader;
    private final MemberAlcoholicDrinksReader memberAlcoholicDrinksReader;
    private final MemberAlcoholicDrinksWriter memberAlcoholicDrinksWriter;

    /**
     * 프로필 수정
     */
    @Transactional
    public UpdateProfileResponse updateProfile(Member loginMember, UpdateProfileRequest request, MultipartFile image) {
        Member member = memberReader.getByEmail(loginMember.getEmail());
        String profileImageUrl = processProfileImage(image);

        // 프로필 업데이트
        member.updateProfile(request, profileImageUrl);

        memberAlcoholTypeWriter.deleteAllByMember(member);

        // 알콜 타입 업데이트
        updateMemberAlcoholTypes(member, request.alcoholTypeIds());

        return new UpdateProfileResponse(member.getId());
    }

    /**
     * 프로필 이미지 처리
     */
    private String processProfileImage(MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            UploadImageInfo uploadImageInfo = s3Service.uploadMemberProfileImage(image);
            return uploadImageInfo.ImageUrl();
        }
        return null;
    }

    /**
     * 회원의 알콜 타입 업데이트
     */
    private void updateMemberAlcoholTypes(Member member, List<Long> alcoholTypeIds) {
        if (!CollectionUtils.isEmpty(alcoholTypeIds)) {
            List<MemberAlcoholType> memberAlcoholTypeList = memberUtils.getMemberAlcoholTypeList(
                    member, alcoholTypeIds, alcoholTypeReader);
            if (!memberAlcoholTypeList.isEmpty()) {
                memberAlcoholTypeWriter.storeAll(memberAlcoholTypeList);
            }
        }
    }

    /**
     * 내 공간 정보 조회
     */
    @Transactional(readOnly = true)    
    public MySpaceResponse getMySpace(Member loginMember) {
        Member member = memberReader.getById(loginMember.getId());
        long tastingNoteCount = tastingNoteReader.getMyTastingNoteCount(member);
        long dailyLifeCount = dailyLifeReader.getMyDailyLifeCount(member);
        long followingCount = followReader.countFollowing(member);
        long followerCount = followReader.countFollower(member);

        return new MySpaceResponse(
                member.getId(),
                member.getProfileImage(),
                member.getNickname(),
                member.getIntroduction(),
                member.isHasBadge(),
                tastingNoteCount,
                dailyLifeCount,
                followingCount,
                followerCount,
                0);
    }

    /**
     * 내 정보 조회
     */
    @Transactional(readOnly = true)
    public MyInfoResponse getMyInfo(Member loginMember) {
        Member member = memberReader.getById(loginMember.getId());
        List<Long> alcoholTypeIdList = memberAlcoholTypeReader.getIdListByMember(member);
        return new MyInfoResponse(
                member.getId(),
                member.getNickname(),
                member.getEmail(),
                member.isHasBadge(),
                member.isNotificationsAllowed(),
                member.getIntroduction(),
                member.getProfileImage(),
                member.getGender(),
                alcoholTypeIdList);
    }

    /**
     * 타 유저 프로필 조회
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "memberProfile", key = "#memberId", unless = "#result == null")
    public MemberProfileResponse getMemberProfile(Member loginMember, Long memberId) {
        Member member = memberReader.getById(memberId);
        long tastingNoteCount = tastingNoteReader.getTastingNoteCountByMemberId(memberId, loginMember);
        long dailyLifeCount = dailyLifeReader.getDailyLifeCountByMemberId(memberId, loginMember);
        long followingCount = followReader.countFollowing(member);
        long followerCount = followReader.countFollower(member);
        boolean isFollowing = followReader.isFollowing(loginMember, member);

        return new MemberProfileResponse(
                member.getId(),
                member.getNickname(),
                member.getProfileImage(),
                member.getIntroduction(),
                member.isHasBadge(),
                tastingNoteCount,
                dailyLifeCount,
                followingCount,
                followerCount,
                isFollowing);
    }

    /**
     * ID로 회원 조회
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "memberById", key = "#memberId", unless = "#result == null")
    public Member findById(Long memberId) {
        return memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorCode.MEMBER_NOT_FOUND));
    }

    /**
     * 이메일로 회원 조회
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "memberByEmail", key = "#email", unless = "#result == null")
    public Member getMemberByEmail(String email) {
        return memberReader.getByEmail(email);
    }

    /**
     * 내가 작성한 일상생활 목록 조회
     */
    @Transactional(readOnly = true)
    public MyDailyLifeListResponse loadMyDailyLifeList(Member member, DailyLifeListRequest request) {
        Slice<MyDailyLifeSummary> myDailyLifeList = dailyLifeReader.getAllMyDailyLives(member,
                request.lastDailyLifeId(), request.pageSize());

        return new MyDailyLifeListResponse(myDailyLifeList);
    }

    /**
     * 특정 회원이 작성한 일상생활 목록 조회
     */
    @Transactional(readOnly = true)
    public DailyLifeListResponse loadMemberDailyLifeList(Member loginMember, DailyLifeListRequest request,
            Long memberId) {
        Slice<DailyLifeSummary> dailyLifeList = dailyLifeReader.getAllDailyLivesByMember(loginMember, memberId,
                request.lastDailyLifeId(), request.pageSize());

        return new DailyLifeListResponse(dailyLifeList);
    }

    /**
     * 내가 작성한 시음노트 목록 조회
     */
    @Transactional(readOnly = true)
    public MyTastingNoteListResponse loadMyTastingNoteList(Member member, TastingNoteListRequest request) {
        Slice<MyTastingNoteSummary> myTastingNoteList = tastingNoteReader.getAllMyTastingNotes(member,
                request.lastTastingNoteId(), request.pageSize());

        return new MyTastingNoteListResponse(myTastingNoteList);
    }

    /**
     * 특정 회원이 작성한 시음노트 목록 조회
     */
    @Transactional(readOnly = true)
    public TastingNoteListResponse loadMemberTastingNoteList(Member loginMember, TastingNoteListRequest request,
            Long memberId) {
        Slice<TastingNoteSummary> tastingNoteList = tastingNoteReader.getAllTastingNotesByMember(loginMember, memberId,
                request.lastTastingNoteId(), request.pageSize());

        return new TastingNoteListResponse(tastingNoteList);
    }

    /**
     * 전통주 저장하기 또는 저장 취소
     * 
     * @return true if saved, false if unsaved
     */
    @Transactional
    public boolean saveAlcoholicDrinks(Member member, Long alcoholicDrinksId) {
        AlcoholicDrinks alcoholicDrinks = alcoholicDrinksReader.getById(alcoholicDrinksId);
        Optional<MemberAlcoholicDrinks> memberAlcoholicDrinks = memberAlcoholicDrinksReader
                .findByMemberAndAlcoholicDrinks(member, alcoholicDrinks);

        // 전통주가 이미 저장되어 있다면 삭제, 저장되어 있지 않다면 등록
        return memberAlcoholicDrinks
                .map(save -> {
                    memberAlcoholicDrinksWriter.delete(save);
                    return false;
                })
                .orElseGet(() -> {
                    memberAlcoholicDrinksWriter.store(member, alcoholicDrinks);
                    return true;
                });
    }

    /**
     * 내가 저장한 전통주 목록 조회
     */
    @Transactional(readOnly = true)
    public MyAlcoholicDrinksListResponse loadMyAlcoholicDrinks(Member member, MyAlcoholicDrinksListRequest request) {
        Slice<AlcoholicDrinksSummary> alcoholicDrinksSummaries = alcoholicDrinksReader.getAllMyAlcoholicDrinks(member,
                request.lastAlcoholicDrinksId(), request.pageSize());

        return new MyAlcoholicDrinksListResponse(alcoholicDrinksSummaries);
    }

}
