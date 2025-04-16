package com.juu.juulabel.follow.repository;

import com.juu.juulabel.common.annotation.Writer;
import com.juu.juulabel.follow.domain.Follow;
import com.juu.juulabel.member.domain.Member;
import com.juu.juulabel.follow.repository.jpa.FollowJpaRepository;
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

    public boolean deleteFollow(final Follow follow){
        if(follow == null){
            return false;
        }else{
            this.unfollow(follow);
            return false;
            }
    }

    private void follow(final Member follower, final Member followee) {
        final Follow follow = Follow.create(follower, followee);
        followJpaRepository.save(follow);
        followJpaRepository.flush();
    }

    private void unfollow(Follow follow) {
        followJpaRepository.delete(follow);
        followJpaRepository.flush();
    }


}
