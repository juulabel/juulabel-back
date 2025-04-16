package com.juu.juulabel.follow.repository.jpa;

import com.juu.juulabel.follow.domain.Follow;
import com.juu.juulabel.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowJpaRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowerAndFollowee(Member follower, Member followee);

}
