package com.juu.juulabel.follow.repository;

import com.juu.juulabel.common.dto.response.RecommendListResponse;
import com.juu.juulabel.common.annotation.Reader;
import com.juu.juulabel.follow.request.FollowUser;
import com.juu.juulabel.follow.domain.Follow;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.follow.repository.jpa.FollowJpaRepository;
import com.juu.juulabel.follow.repository.query.FollowQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;

@Reader
@RequiredArgsConstructor
public class FollowReader {

    private final FollowJpaRepository followJpaRepository;
    private final FollowQueryRepository followQueryRepository;

    public Follow findOrNullByFollowerAndFollowee(final Member follower, final Member followee) {
        return followJpaRepository.findByFollowerAndFollowee(follower, followee)
                .orElse(null);
    }

    public Slice<FollowUser> findAllFollowing(final Member loginMember,
                                              final Member member,
                                              final Long lastFollowId,
                                              final int pageSize) {
        return followQueryRepository.findAllFollowing(loginMember, member, lastFollowId, pageSize);
    }

    public Slice<FollowUser> findAllFollower(final Member loginMember,
                                             final Member member,
                                             final Long lastFollowId,
                                             final int pageSize) {
        return followQueryRepository.findAllFollower(loginMember, member, lastFollowId, pageSize);
    }

    public Slice<FollowUser> getSearchFollowUser(final Member loginMember,
                                                 final Long lastFollowId,
                                                 final int pageSize,
                                                 final String username) {
        return followQueryRepository.getSearchUserList(loginMember,lastFollowId, pageSize, username);
    }

    public RecommendListResponse getRecommendUserList(final Member loginMember,
                                                      final Long badgeLastUserId,
                                                      final Long tastingLastUserId,
                                             final int pageSize) {

        Slice<FollowUser> badgeRecommendUsers = followQueryRepository.findBadgeRecommendUserList(loginMember, badgeLastUserId, pageSize);
        Slice<FollowUser> tastingRecommendUsers = followQueryRepository.findTastingRecommendUserList(loginMember, tastingLastUserId, pageSize);

        return new RecommendListResponse(badgeRecommendUsers, tastingRecommendUsers);
    }

    public long countFollowing(final Member member){
        return followQueryRepository.countFollowing(member);
    }

    public long countFollower(final Member member){
        return followQueryRepository.countFollower(member);
    }

    public boolean isFollowing(final Member loginMember , final Member member){
        return followQueryRepository.isFollowing(loginMember,member);
    }
}
