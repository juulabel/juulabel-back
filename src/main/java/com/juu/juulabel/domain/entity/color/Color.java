package com.juu.juulabel.domain.entity.color;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "color"
)
public class Color {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '컬러 고유 번호'")
    private Long id;

    @Column(name = "name", columnDefinition = "varchar(20) comment '컬러 이름(한국어)'")
    private String name;

    @Column(name = "rgb", columnDefinition = "varchar(7) comment 'RGB 값 (#000000)'")
    private String rgb;

}