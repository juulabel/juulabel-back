package com.juu.juulabel.domain.entity.alcohol;

import com.juu.juulabel.domain.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "alcohol_type_sensory")
public class AlcoholTypeSensory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '주종별 촉각 매핑 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alcohol_type_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '전통주 주종 고유 번호'")
    private AlcoholType alcoholType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensory_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '촉각 고유 번호'")
    private Sensory sensory;

    @Column(name = "is_used", columnDefinition = "tinyint comment '사용 여부'")
    private boolean isUsed;

}
