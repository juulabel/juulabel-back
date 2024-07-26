package com.juu.juulabel.domain.entity.alcohol;

import com.juu.juulabel.domain.base.BaseTimeEntity;
import com.juu.juulabel.domain.converter.LevelConverter;
import com.juu.juulabel.domain.embedded.AlcoholSnapshot;
import com.juu.juulabel.domain.entity.color.Color;
import com.juu.juulabel.domain.enums.alcohol.sensory.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "tasting_note"
)
public class TastingNote extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '시음노트 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alcohol_type_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '주종 ID'")
    private AlcoholType alcoholType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alcoholic_drinks_id", columnDefinition = "BIGINT UNSIGNED comment '전통주 ID'")
    private AlcoholicDrinks alcoholicDrinks;

    @Embedded
    private AlcoholSnapshot alcoholSnapshot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id", columnDefinition = "BIGINT UNSIGNED comment '색상 ID'")
    private Color color;

    @Convert(converter = LevelConverter.class)
    @Column(name = "carbonation", columnDefinition = "int comment '탄산도'")
    private CarbonationLevel carbonation;

    @Convert(converter = LevelConverter.class)
    @Column(name = "clarity", columnDefinition = "int comment '투명도'")
    private ClarityLevel clarity;

    @Convert(converter = LevelConverter.class)
    @Column(name = "viscosity", columnDefinition = "int comment '점성도'")
    private ViscosityLevel viscosity;

    @Convert(converter = LevelConverter.class)
    @Column(name = "sediment", columnDefinition = "int comment '침전물'")
    private SedimentLevel sediment;

    @Convert(converter = LevelConverter.class)
    @Column(name = "density", columnDefinition = "int comment '진하기'")
    private DensityLevel density;

    @Convert(converter = LevelConverter.class)
    @Column(name = "turbidity", columnDefinition = "int comment '탁도'")
    private TurbidityLevel turbidity;

    @Column(name = "content", columnDefinition = "varchar(2000) comment '내용'")
    private String content;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '삭제 일시'")
    private LocalDateTime deletedAt;
}
