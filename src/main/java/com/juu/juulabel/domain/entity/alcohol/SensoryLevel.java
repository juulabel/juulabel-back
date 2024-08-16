package com.juu.juulabel.domain.entity.alcohol;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "sensory_level"
)
public class SensoryLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '촉각 점수 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensory", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '촉각 ID'")
    private Sensory sensory;

    @Column(name = "score", columnDefinition = "int comment '촉각 점수(한국어)'")
    private int score;

    @Column(name = "description", columnDefinition = "varchar(20) comment '촉각 설명(한국어)'")
    private String description;

}
