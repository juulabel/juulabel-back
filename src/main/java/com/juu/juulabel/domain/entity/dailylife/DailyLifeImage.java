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
    name = "daily_life_image"
)
public class DailyLifeImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '파일 고유 번호'")
    private Long id;

    @Column(name = "image_name", nullable = false, columnDefinition = "VARCHAR(255) comment '이미지 파일 이름'")
    private String name;

    @Column(name = "image_path", nullable = false, columnDefinition = "VARCHAR(255) comment '이미지 파일 경로'")
    private String path;

    @Column(name = "image_extension", nullable = false, columnDefinition = "VARCHAR(20) comment '이미지 파일 확장자'")
    private String extension;

    @Column(name = "image_size", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '이미지 파일 크기'")
    private Long size;

}