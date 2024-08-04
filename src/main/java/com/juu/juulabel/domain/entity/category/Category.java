package com.juu.juulabel.domain.entity.category;

import com.juu.juulabel.domain.base.BaseTimeEntity;
import com.juu.juulabel.domain.enums.category.CategoryType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "category"
)
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '카테고리 고유 번호'")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type", columnDefinition = "varchar(50) comment '타입'")
    private CategoryType type;

    @Column(name = "code", nullable = false, columnDefinition = "varchar(20) comment '코드'")
    private String code;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(20) comment '카테고리명'")
    private String name;

    @Column(name = "is_used", columnDefinition = "tinyint comment '사용 여부'")
    private boolean isUsed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", columnDefinition = "bigint unsigned comment '카테고리 부모 번호'")
    private Category parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Category> children = new ArrayList<>();

}
