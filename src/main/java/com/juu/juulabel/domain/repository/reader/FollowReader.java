package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.dto.follow.FollowUser;
import com.juu.juulabel.domain.entity.follow.Follow;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.repository.jpa.FollowJpaRepository;
import com.juu.juulabel.domain.repository.query.FollowQueryRepository;
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
}
