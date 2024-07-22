package com.juu.juulabel.domain.entity.follow;


import com.juu.juulabel.domain.base.BaseTimeEntity;
import com.juu.juulabel.domain.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "follow"
)
public class Follow extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '팔로우 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '팔로워(팔로우 진행)'")
    private Member follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followee_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '팔로위(팔로우 수락)'")
    private Member followee;

    @Column(name = "followed_at", columnDefinition = "datetime comment '팔로우 일시'")
    private LocalDateTime followedAt;

    public static Follow create(Member follower,
                                Member followee) {
        return Follow.builder()
                .follower(follower)
                .followee(followee)
                .followedAt(LocalDateTime.now())
                .build();
    }

}
