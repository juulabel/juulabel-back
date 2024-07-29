package com.juu.juulabel.domain.entity.dailylife;

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
    name = "daily_life_image"
)
public class DailyLifeImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '파일 고유 번호'")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "daily_life_id", columnDefinition = "BIGINT UNSIGNED comment '일상생활 고유 번호'")
    private DailyLife dailyLife;

    @Column(name = "seq", columnDefinition = "INT comment '이미지 순서'")
    private int seq;

    @Column(name = "image_path", columnDefinition = "VARCHAR(255) comment '이미지 경로'")
    private String imagePath;

    @Column(name = "deleted_at", columnDefinition = "DATETIME comment '삭제 일시'")
    private LocalDateTime deletedAt;

    public static DailyLifeImage create(DailyLife dailyLife, int seq, String imagePath) {
        return DailyLifeImage.builder()
            .dailyLife(dailyLife)
            .seq(seq)
            .imagePath(imagePath)
            .build();
    }

}