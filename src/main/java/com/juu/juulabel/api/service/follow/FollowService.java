package com.juu.juulabel.api.service.follow;

import com.juu.juulabel.api.dto.request.FollowOrUnfollowRequest;
import com.juu.juulabel.api.dto.request.LoadFollowerListRequest;
import com.juu.juulabel.api.dto.request.LoadFollowingListRequest;
import com.juu.juulabel.api.dto.response.FollowOrUnfollowResponse;
import com.juu.juulabel.api.dto.response.LoadFollowerListResponse;
import com.juu.juulabel.api.dto.response.LoadFollowingListResponse;
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
    public LoadFollowingListResponse loadFollowingList(final Member loginMember, final Long memberId, final LoadFollowingListRequest request) {
        final Member member = memberReader.getById(memberId);
        final Slice<FollowUser> followingList = followReader.findAllFollowing(loginMember, member, request.lastFollowId(), request.pageSize());
        return new LoadFollowingListResponse(followingList);
    }

    @Transactional(readOnly = true)
    public LoadFollowerListResponse loadFollowerList(final Member loginMember, final Long memberId, final LoadFollowerListRequest request) {
        final Member member = memberReader.getById(memberId);
        final Slice<FollowUser> followingList = followReader.findAllFollower(loginMember, member, request.lastFollowId(), request.pageSize());
        return new LoadFollowerListResponse(followingList);
    }

}
