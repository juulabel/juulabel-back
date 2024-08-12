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
        name = "tasting_note_image"
)
public class TastingNoteImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '이미지 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tasting_note_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '시음노트 ID'")
    private TastingNote tastingNote;

    @Column(name = "seq", columnDefinition = "int comment '이미지 순서'")
    private int seq;

    @Column(name = "image_path", columnDefinition = "varchar(255) comment '이미지 경로'")
    private String imagePath;

    /**
     * 1. 이미지 삭제 후 스케줄링을 통해 주기적으로 S3 이미지 삭제
     * 2. 이미지 삭제시 S3 데이터 바로 삭제, deletedAt 관리 x
     */
    @Column(name = "deleted_at", columnDefinition = "datetime comment '삭제 일시'")
    private LocalDateTime deletedAt;

}
