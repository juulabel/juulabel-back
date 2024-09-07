package com.juu.juulabel.domain.entity.alcohol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.juu.juulabel.domain.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "alcoholic_drinks"
)
public class AlcoholicDrinks extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '전통주 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alcohol_type_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '주종 ID'")
    private AlcoholType alcoholType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brewery_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '양조장 ID'")
    private Brewery brewery;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(100) comment '전통주 이름'")
    private String name;

    @Column(name = "description", columnDefinition = "text comment '설명'")
    private String description;

    @Column(name = "alcohol_content", columnDefinition = "decimal(4,2) comment '알코올 도수'")
    private Double alcoholContent;

    @Column(name = "volume", columnDefinition = "decimal(10,2) comment '용량'")
    private double volume;

    @Column(name = "price", columnDefinition = "DECIMAL(12,2) comment '가격'")
    private double price;

    @Column(name = "image", columnDefinition = "varchar(255) comment '이미지'")
    private String image;

    @Column(name = "ingredients", columnDefinition = "text comment '원재료'")
    private String ingredients;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '삭제 일시'")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "alcoholicDrinks", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TastingNote> tastingNotes = new ArrayList<>();

}
