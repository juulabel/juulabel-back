package com.juu.juulabel.domain.entity.alcohol;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "flavor_level"
)
public class FlavorLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '미각 점수 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flavor", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '맛 ID'")
    private Flavor flavor;

    @Column(name = "score", columnDefinition = "int comment '맛 점수(한국어)'")
    private int score;

    @Column(name = "description", columnDefinition = "varchar(20) comment '맛 설명(한국어)'")
    private String description;

}
