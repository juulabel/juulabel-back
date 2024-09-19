package com.juu.juulabel.domain.entity.tastingnote;

import com.juu.juulabel.domain.base.BaseCreatedTimeEntity;
import com.juu.juulabel.domain.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
    name = "tasting_note_comment_like"
)
public class TastingNoteCommentLike extends BaseCreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '시음노트 좋아요 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tasting_note_comment_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '시음노트 댓글 고유 번호'")
    private TastingNoteComment tastingNoteComment;

    public static TastingNoteCommentLike create(Member member, TastingNoteComment tastingNoteComment) {
        return TastingNoteCommentLike.builder()
            .member(member)
            .tastingNoteComment(tastingNoteComment)
            .build();
    }

}
