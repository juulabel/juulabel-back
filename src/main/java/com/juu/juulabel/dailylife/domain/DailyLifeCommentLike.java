package com.juu.juulabel.dailylife.domain;

import com.juu.juulabel.common.base.BaseCreatedTimeEntity;
import com.juu.juulabel.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
    name = "daily_life_comment_like"
)
public class DailyLifeCommentLike extends BaseCreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '일상생활 좋아요 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_life_comment_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '일상생활 댓글 고유 번호'")
    private DailyLifeComment dailyLifeComment;

    public static DailyLifeCommentLike create(Member member, DailyLifeComment dailyLifeComment) {
        return DailyLifeCommentLike.builder()
            .member(member)
            .dailyLifeComment(dailyLifeComment)
            .build();
    }

}