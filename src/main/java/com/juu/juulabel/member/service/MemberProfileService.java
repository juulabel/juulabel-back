package com.juu.juulabel.member.service;

import com.juu.juulabel.alcohol.repository.AlcoholTypeReader;
import com.juu.juulabel.alcohol.repository.TastingNoteReader;
import com.juu.juulabel.common.dto.request.UpdateProfileRequest;
import com.juu.juulabel.common.dto.response.MemberProfileResponse;
import com.juu.juulabel.common.dto.response.MyInfoResponse;
import com.juu.juulabel.common.dto.response.MySpaceResponse;
import com.juu.juulabel.common.dto.response.UpdateProfileResponse;
import com.juu.juulabel.dailylife.repository.DailyLifeReader;
import com.juu.juulabel.follow.repository.FollowReader;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.member.domain.MemberAlcoholType;
import com.juu.juulabel.member.repository.MemberAlcoholTypeReader;
import com.juu.juulabel.member.repository.MemberAlcoholTypeWriter;
import com.juu.juulabel.member.repository.MemberReader;
import com.juu.juulabel.member.util.MemberUtils;
import com.juu.juulabel.s3.S3Service;
import com.juu.juulabel.s3.UploadImageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 회원 프로필 관리 서비스
 */
@Service
@RequiredArgsConstructor
public class MemberProfileService {

    private final MemberReader memberReader;
    private final MemberAlcoholTypeWriter memberAlcoholTypeWriter;
    private final MemberAlcoholTypeReader memberAlcoholTypeReader;
    private final AlcoholTypeReader alcoholTypeReader;
    private final S3Service s3Service;
    private final DailyLifeReader dailyLifeReader;
    private final TastingNoteReader tastingNoteReader;
    private final FollowReader followReader;
    private final MemberUtils memberUtils;

    /**
     * 닉네임 중복 확인
     */
    @Transactional(readOnly = true)
    public boolean checkNickname(String nickname) {
        return memberReader.existActiveNickname(nickname);
    }

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
}