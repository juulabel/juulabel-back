package com.juu.juulabel.domain.entity.alcohol;

import com.juu.juulabel.domain.base.BaseTimeEntity;
import com.juu.juulabel.domain.entity.category.Category;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "alcohol_type_scent")
public class AlcoholTypeScent extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '주종별 향 매핑 고유 번호'")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", columnDefinition = "BIGINT UNSIGNED comment '카테고리 고유 번호'")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alcohol_type_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '전통주 주종 고유 번호'")
    private AlcoholType alcoholType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scent_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '향기 고유 번호'")
    private Scent scent;

    @Column(name = "is_used", columnDefinition = "tinyint comment '사용 여부'")
    private boolean isUsed;

}
