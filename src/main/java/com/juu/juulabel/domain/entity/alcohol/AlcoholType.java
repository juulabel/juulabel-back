package com.juu.juulabel.domain.entity.alcohol;

import com.juu.juulabel.domain.base.BaseTimeEntity;
import com.juu.juulabel.domain.entity.tastingnote.TastingNote;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
    name = "alcohol_type"
)
public class AlcoholType extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '전통주 주종 고유 번호'")
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(100) comment '전통주 주종 이름'")
    private String name;

    @Column(name = "image", columnDefinition = "varchar(255) comment '이미지'")
    private String image;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '삭제 일시'")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "alcoholType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TastingNote> tastingNotes = new ArrayList<>();

}
