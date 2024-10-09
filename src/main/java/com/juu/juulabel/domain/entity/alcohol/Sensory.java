package com.juu.juulabel.domain.entity.alcohol;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "sensory"
)
public class Sensory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '촉각 고유 번호'")
    private Long id;

    @Column(name = "name", columnDefinition = "varchar(20) comment '촉각 이름(한국어)'")
    private String name;

    @Column(name = "description", columnDefinition = "varchar(40) comment '촉각 설명'")
    private String description;

}
