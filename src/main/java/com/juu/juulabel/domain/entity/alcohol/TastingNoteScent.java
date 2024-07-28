package com.juu.juulabel.domain.entity.alcohol;

import com.juu.juulabel.domain.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

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
    @JoinColumn(name = "scent_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '향기 ID'")
    private Scent scent;

}
