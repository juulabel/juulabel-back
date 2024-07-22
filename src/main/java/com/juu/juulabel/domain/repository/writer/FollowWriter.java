package com.juu.juulabel.domain.repository.writer;

import com.juu.juulabel.domain.annotation.Writer;
import com.juu.juulabel.domain.entity.follow.Follow;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.repository.jpa.FollowJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Writer
@RequiredArgsConstructor
public class FollowWriter {

    private final FollowJpaRepository followJpaRepository;

    public boolean followOrUnfollow(final Follow follow, final Member follower, final Member followee) {
        if (Objects.isNull(follow)) {
            this.follow(follower, followee);
            return true;
        } else {
            this.unfollow(follow);
            return false;
        }
    }

    private void follow(final Member follower, final Member followee) {
        final Follow follow = Follow.create(follower, followee);
        followJpaRepository.save(follow);
    }

    private void unfollow(Follow follow) {
        followJpaRepository.delete(follow);
    }

}
