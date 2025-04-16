package com.juu.juulabel.domain.entity.tastingnote;

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
    name = "tasting_note_comment"
)
public class TastingNoteComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '시음노트 댓글 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tasting_note_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '시음노트 고유 번호'")
    private TastingNote tastingNote;

    @Column(name = "content", nullable = false, columnDefinition = "VARCHAR(600) comment '댓글 내용'")
    private String content;

    @ManyToOne
    @JoinColumn(name = "parent_id", columnDefinition = "BIGINT UNSIGNED comment '상위 댓글 ID'")
    private TastingNoteComment parent;

    @Column(name = "deleted_at", columnDefinition = "DATETIME comment '삭제 일시'")
    private LocalDateTime deletedAt;

    public static TastingNoteComment createComment(Member member, TastingNote tastingNote, String content) {
        return TastingNoteComment.builder()
            .member(member)
            .tastingNote(tastingNote)
            .content(content)
            .build();
    }

    public static TastingNoteComment createReply(Member member, TastingNote tastingNote, String content, TastingNoteComment parent) {
        return TastingNoteComment.builder()
            .member(member)
            .tastingNote(tastingNote)
            .content(content)
            .parent(parent)
            .build();
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void delete() {
        if (this.deletedAt != null) {
            throw new InvalidParamException(ErrorCode.ALREADY_DELETED_TASTING_NOTE);
        }

        this.deletedAt = LocalDateTime.now();
    }
}
