package com.juu.juulabel.domain.entity.alcohol;

import com.juu.juulabel.domain.base.BaseTimeEntity;
import com.juu.juulabel.domain.entity.color.Color;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "alcohol_color"
)
public class AlcoholColor extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '주종별 색상 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liquor_type_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '전통주 주종 고유 번호'")
    private AlcoholType alcoholType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '색상 고유 번호'")
    private Color color;

}