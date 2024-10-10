package com.juu.juulabel.domain.entity.alcohol;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "alcoholic_drinks_ingredient")
public class AlcoholicDrinksIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '선호전통주 주종 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alcoholic_drinks_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '전통주 고유 번호'")
    private AlcoholicDrinks alcoholicDrinks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '원재료 고유 번호'")
    private Ingredient ingredient;

}
