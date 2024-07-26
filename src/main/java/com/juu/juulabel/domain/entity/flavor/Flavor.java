package com.juu.juulabel.domain.entity.flavor;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '향 고유 번호'")
    private Long id;

    // TODO : 테이블 or ENUM
    @Column(name = "category", columnDefinition = "varchar(20) comment '카테고리'")
    private String category;

    @Column(name = "name", columnDefinition = "varchar(20) comment '향'")
    private String name;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '삭제 일시'")
    private LocalDateTime deletedAt;
}
