package com.juu.juulabel.api.service.follow;

import com.juu.juulabel.api.dto.request.FollowOrUnfollowRequest;
import com.juu.juulabel.api.dto.request.FollowerListRequest;
import com.juu.juulabel.api.dto.request.FollowingListRequest;
import com.juu.juulabel.api.dto.response.FollowOrUnfollowResponse;
import com.juu.juulabel.api.dto.response.FollowerListResponse;
import com.juu.juulabel.api.dto.response.FollowingListResponse;
import com.juu.juulabel.domain.dto.follow.FollowUser;
import com.juu.juulabel.domain.entity.follow.Follow;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.repository.reader.FollowReader;
import com.juu.juulabel.domain.repository.reader.MemberReader;
import com.juu.juulabel.domain.repository.writer.FollowWriter;
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
        return new FollowingListResponse(followingList);
    }

    @Transactional(readOnly = true)
    public FollowerListResponse loadFollowerList(final Member loginMember, final Long memberId, final FollowerListRequest request) {
        final Member member = memberReader.getById(memberId);
        final Slice<FollowUser> followingList = followReader.findAllFollower(loginMember, member, request.lastFollowId(), request.pageSize());
        return new FollowerListResponse(followingList);
    }

}
