package com.juu.juulabel.domain.entity.alcohol;

import com.juu.juulabel.domain.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "tasting_note_sensory_level"
)
public class TastingNoteSensoryLevel extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '시음노트-촉각 연결 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tasting_note_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '시음노트 ID'")
    private TastingNote tastingNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensory_level_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '촉각 점수 ID'")
    private SensoryLevel sensoryLevel;

    public static TastingNoteSensoryLevel of(TastingNote tastingNote, SensoryLevel sensoryLevel) {
        return TastingNoteSensoryLevel.builder()
                .tastingNote(tastingNote)
                .sensoryLevel(sensoryLevel)
                .build();
    }

    public static List<TastingNoteSensoryLevel> of(TastingNote tastingNote, List<SensoryLevel> sensoryLevels) {
        return sensoryLevels.stream()
                .map(sensoryLevel -> TastingNoteSensoryLevel.of(tastingNote, sensoryLevel))
                .toList();
    }

}
