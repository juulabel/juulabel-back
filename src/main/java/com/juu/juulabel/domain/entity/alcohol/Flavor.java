package com.juu.juulabel.domain.entity.alcohol;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "flavor"
)
public class Flavor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '맛 고유 번호'")
    private Long id;

    @Column(name = "name", columnDefinition = "varchar(20) comment '맛 이름(한국어)'")
    private String name;

}
