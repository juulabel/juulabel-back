package com.juu.juulabel.domain.entity.tastingnote;

import com.juu.juulabel.domain.base.BaseTimeEntity;
import com.juu.juulabel.domain.entity.alcohol.FlavorLevel;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "tasting_note_flavor_level"
)
public class TastingNoteFlavorLevel extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '시음노트-맛 연결 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tasting_note_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '시음노트 ID'")
    private TastingNote tastingNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flavor_level_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '맛 점수 ID'")
    private FlavorLevel flavorLevel;

    public static TastingNoteFlavorLevel of(TastingNote tastingNote, FlavorLevel flavorLevel) {
        return TastingNoteFlavorLevel.builder()
                .tastingNote(tastingNote)
                .flavorLevel(flavorLevel)
                .build();
    }

    public static List<TastingNoteFlavorLevel> of(TastingNote tastingNote, List<FlavorLevel> flavorLevels) {
        return flavorLevels.stream()
                .map(flavorLevel -> TastingNoteFlavorLevel.of(tastingNote, flavorLevel))
                .toList();
    }

}
