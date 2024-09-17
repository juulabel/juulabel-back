package com.juu.juulabel.domain.entity.tastingnote;

import com.juu.juulabel.api.annotation.Rating;
import com.juu.juulabel.domain.base.BaseTimeEntity;
import com.juu.juulabel.domain.embedded.AlcoholicDrinksSnapshot;
import com.juu.juulabel.domain.entity.alcohol.AlcoholType;
import com.juu.juulabel.domain.entity.alcohol.AlcoholicDrinks;
import com.juu.juulabel.domain.entity.alcohol.Color;
import com.juu.juulabel.domain.entity.member.Member;
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
        name = "tasting_note"
)
public class TastingNote extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '시음노트 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alcohol_type_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '주종 ID'")
    private AlcoholType alcoholType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alcoholic_drinks_id", columnDefinition = "BIGINT UNSIGNED comment '전통주 ID'")
    private AlcoholicDrinks alcoholicDrinks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id", columnDefinition = "BIGINT UNSIGNED comment '색상 ID'")
    private Color color;

    @Embedded
    private AlcoholicDrinksSnapshot alcoholDrinksInfo;

    @Rating
    @Column(name = "rating", nullable = false, columnDefinition = "DECIMAL(3,2) comment '평점 (0.00 - 5.00)'")
    private Double rating;

    @Column(name = "content", columnDefinition = "varchar(2000) comment '내용'")
    private String content;

    @Column(name = "is_private", columnDefinition = "tinyint comment '비공개 여부'")
    private boolean isPrivate;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '삭제 일시'")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "tastingNote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TastingNoteScent> tastingNoteScents = new ArrayList<>();


    public static TastingNote of(Member member,
                                 AlcoholType alcoholType,
                                 AlcoholicDrinks alcoholicDrinks,
                                 Color color,
                                 AlcoholicDrinksSnapshot alcoholDrinksInfo,
                                 Double rating,
                                 String content,
                                 boolean isPrivate) {
        return TastingNote.builder()
                .member(member)
                .alcoholType(alcoholType)
                .alcoholicDrinks(alcoholicDrinks)
                .color(color)
                .alcoholDrinksInfo(alcoholDrinksInfo)
                .rating(rating)
                .content(content)
                .isPrivate(isPrivate)
                .build();
    }

}
