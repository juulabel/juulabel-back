package com.juu.juulabel.domain.entity.alcohol;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "alcoholic_drinks_pairing"
)
public class AlcoholicDrinksPairing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment ' 전통주 페어링 매핑 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alcoholic_drinks_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '전통주 ID'")
    private AlcoholicDrinks alcoholicDrinks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pairing_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '페어링 ID'")
    private Pairing pairing;
}
