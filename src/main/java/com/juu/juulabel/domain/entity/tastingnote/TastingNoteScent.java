package com.juu.juulabel.domain.entity.tastingnote;

import com.juu.juulabel.domain.base.BaseTimeEntity;
import com.juu.juulabel.domain.entity.alcohol.Scent;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "tasting_note_scent")
public class TastingNoteScent extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '시음노트-향기 매핑 고유 번호'")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tasting_note_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '시음노트 ID'")
    private TastingNote tastingNote;

    @ManyToOne
    @JoinColumn(name = "scent_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '향 ID'")
    private Scent scent;

    public static TastingNoteScent of(TastingNote tastingNote, Scent scent) {
        return TastingNoteScent.builder()
                .tastingNote(tastingNote)
                .scent(scent)
                .build();
    }

    public static List<TastingNoteScent> of(TastingNote tastingNote, List<Scent> scents) {
        return scents.stream()
                .map(scent -> TastingNoteScent.of(tastingNote, scent))
                .toList();
    }

}
