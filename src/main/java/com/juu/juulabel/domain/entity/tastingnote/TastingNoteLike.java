package com.juu.juulabel.domain.entity.tastingnote;

import com.juu.juulabel.domain.base.BaseCreatedTimeEntity;
import com.juu.juulabel.domain.entity.alcohol.TastingNote;
import com.juu.juulabel.domain.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
    name = "tasting_note_like"
)
public class TastingNoteLike extends BaseCreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '시음노트 좋아요 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tasting_note_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '시음노트 고유 번호'")
    private TastingNote tastingNote;

    public static TastingNoteLike create(Member member, TastingNote tastingNote) {
        return TastingNoteLike.builder()
            .member(member)
            .tastingNote(tastingNote)
            .build();
    }

}
