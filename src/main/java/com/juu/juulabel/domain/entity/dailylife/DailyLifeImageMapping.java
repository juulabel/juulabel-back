package com.juu.juulabel.domain.entity.dailylife;

import com.juu.juulabel.domain.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
    name = "daily_life_image_mapping"
)
public class DailyLifeImageMapping extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '약관 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_life_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '일상생활 고유 번호'")
    private DailyLife dailyLife;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_life_image_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '일상생활 이미지 고유 번호'")
    private DailyLifeImage dailyLifeImage;

}