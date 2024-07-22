package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.follow.Follow;
import com.juu.juulabel.domain.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowJpaRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowerAndFollowee(Member follower, Member followee);

}
