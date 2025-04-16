package com.juu.juulabel.domain.entity.dailylife;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
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
    name = "daily_life"
)
public class DailyLife extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '일상생활 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Member member;

    @Column(name = "title", nullable = false, columnDefinition = "VARCHAR(30) comment '제목'")
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "VARCHAR(1200) comment '내용'")
    private String content;

    @Column(name = "is_private", nullable = false, columnDefinition = "TINYINT(1) comment '비밀글 여부'")
    private boolean isPrivate;

    @Column(name = "deleted_at", columnDefinition = "DATETIME comment '삭제 일시'")
    private LocalDateTime deletedAt;

    public static DailyLife create(Member member, String title, String content, boolean isPrivate) {
        return DailyLife.builder()
            .member(member)
            .title(title)
            .content(content)
            .isPrivate(isPrivate)
            .build();
    }

    public void delete() {
        if (this.deletedAt != null) {
            throw new InvalidParamException(ErrorCode.ALREADY_DELETED_DAILY_LIFE);
        }

        this.deletedAt = LocalDateTime.now();
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
}