package com.juu.juulabel.domain.entity.alcohol;

import com.juu.juulabel.domain.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "brewery"
)
public class Brewery extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '양조장 고유 번호'")
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(100) comment '양조장 이름'")
    private String name;

    @Column(name = "region", columnDefinition = "varchar(255) comment '양조장 지역'")
    private String region;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '삭제 일시'")
    private LocalDateTime deletedAt;

}
