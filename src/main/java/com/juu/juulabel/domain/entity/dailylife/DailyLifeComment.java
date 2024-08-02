package com.juu.juulabel.domain.entity.dailylife;

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
    name = "daily_life_comment"
)
public class DailyLifeComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '일상생활 댓글 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_life_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '일상생활 고유 번호'")
    private DailyLife dailyLife;

    @Column(name = "content", nullable = false, columnDefinition = "MEDIUMTEXT comment '댓글 내용'")
    private String content;

    @ManyToOne
    @JoinColumn(name = "parent_id", columnDefinition = "BIGINT UNSIGNED comment '상위 댓글 ID'")
    private DailyLifeComment parent;

    @Column(name = "deleted_at", columnDefinition = "DATETIME comment '삭제 일시'")
    private LocalDateTime deletedAt;

    public static DailyLifeComment createReply(Member member,DailyLife dailyLife, String content, DailyLifeComment parent) {
        return DailyLifeComment.builder()
            .member(member)
            .dailyLife(dailyLife)
            .content(content)
            .parent(parent)
            .build();
    }

    public static DailyLifeComment createComment(Member member,DailyLife dailyLife, String content) {
        return DailyLifeComment.builder()
            .member(member)
            .dailyLife(dailyLife)
            .content(content)
            .build();
    }

    public void updateContent(String content) {
        this.content = content;
    }

}