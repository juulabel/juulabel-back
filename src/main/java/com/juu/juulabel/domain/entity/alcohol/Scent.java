package com.juu.juulabel.domain.entity.alcohol;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "scent")
public class Scent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '향 고유 번호'")
    private Long id;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "type", nullable = false, columnDefinition = "varchar(20) comment '향 타입'")
//    private ScentType type;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(100) comment '향 이름'")
    private String name;

}