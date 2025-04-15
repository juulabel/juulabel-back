package com.juu.juulabel.follow.service;

import com.juu.juulabel.common.dto.request.RecommendListRequest;
import com.juu.juulabel.common.dto.request.SearchUserListRequest;
import com.juu.juulabel.common.dto.response.*;
import com.juu.juulabel.follow.repository.FollowReader;
import com.juu.juulabel.member.repository.MemberReader;
import com.juu.juulabel.follow.repository.FollowWriter;
import com.juu.juulabel.follow.domain.Follow;
import com.juu.juulabel.follow.request.*;
import com.juu.juulabel.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowWriter followWriter;
    private final FollowReader followReader;
    private final MemberReader memberReader;

    @Transactional
    public FollowOrUnfollowResponse followOrUnfollow(final Member follower, final FollowOrUnfollowRequest request) {
        final Member followee = memberReader.getById(request.followeeId());
        final Follow follow = followReader.findOrNullByFollowerAndFollowee(follower, followee);
        final boolean isFollowed = followWriter.followOrUnfollow(follow, follower, followee);
        return new FollowOrUnfollowResponse(isFollowed);
    }

    @Transactional(readOnly = true)
    public FollowingListResponse loadFollowingList(final Member loginMember, final Long memberId, final FollowingListRequest request) {
        final Member member = memberReader.getById(memberId);
        final Slice<FollowUser> followingList = followReader.findAllFollowing(loginMember, member, request.lastFollowId(), request.pageSize());
        final long count = followReader.countFollowing(member);
        return new FollowingListResponse(count,followingList);
    }

    @Transactional(readOnly = true)
    public FollowerListResponse loadFollowerList(final Member loginMember, final Long memberId, final FollowerListRequest request) {
        final Member member = memberReader.getById(memberId);
        final Slice<FollowUser> followingList = followReader.findAllFollower(loginMember, member, request.lastFollowId(), request.pageSize());
        final long count = followReader.countFollower(member);
        return new FollowerListResponse(count , followingList);
    }

    @Transactional(readOnly = true)
    public SearchUserListResponse loadSearchUserList(final Member loginMember, final SearchUserListRequest request) {

        Slice<FollowUser> searchUserList = followReader.getSearchFollowUser(loginMember, request.lastFollowId(), request.pageSize(), request.username());

        return new SearchUserListResponse(searchUserList);
    }

    @Transactional
    public FollowDeleteResponse deleteFollowing(final Member followee, final FollowDeleteRequest request) {
        final Member follower = memberReader.getById(request.followerId());

        // 팔로잉 삭제
        final Follow isFollowing = followReader.findOrNullByFollowerAndFollowee(follower, followee);
        final boolean isFollowingDeleted = followWriter.deleteFollow(isFollowing);

        // 팔로워 삭제
        final Follow isFollower = followReader.findOrNullByFollowerAndFollowee(followee, follower);
        final boolean isFollowerDeleted = followWriter.deleteFollow(isFollower);

        return new FollowDeleteResponse(isFollowingDeleted || isFollowerDeleted);
    }

    @Transactional(readOnly = true)
    public RecommendListResponse loadRecommendList(final Member loginMember, final RecommendListRequest request) {

        return followReader.getRecommendUserList(loginMember, request.badgeLastUserId(), request.tastingLastUserId(), request.pageSize());
    }

}
