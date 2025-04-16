package com.juu.juulabel.domain.entity.alcohol;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "pairing"
)
public class Pairing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '페어링 고유 번호'")
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(100) comment '페어링 이름'")
    private String name;

}
