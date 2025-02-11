package com.juu.juulabel.domain.entity.alcohol;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "ingredient"
)
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '원재료 고유 번호'")
    private Long id;

    @Column(name = "name", columnDefinition = "varchar(20) comment '원재료명'")
    private String name;

}
