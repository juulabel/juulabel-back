package com.juu.juulabel.domain.entity.alcohol;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
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

    @Column(name = "volume", columnDefinition = "int comment '용량'")
    private int volume;

    @Column(name = "discount_price", columnDefinition = "int comment '할인가'")
    private int discountPrice;

    @Column(name = "regular_price", columnDefinition = "int comment '정가'")
    private int regularPrice;

    @Column(name = "image", columnDefinition = "varchar(255) comment '이미지'")
    private String image;

    @Column(name = "tasting_note_count", columnDefinition = "int comment '총 시음노트 개수'")
    private int tastingNoteCount;

    @Column(name = "rating", nullable = false, columnDefinition = "DECIMAL(3,2) comment '평균 달점 (0.00 - 5.00)'")
    private Double rating;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '삭제 일시'")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "alcoholicDrinks", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TastingNote> tastingNotes = new ArrayList<>();

    public void addRating(Double newRating) {
        if (newRating == null || newRating < 0 || newRating > 5) {
            throw new InvalidParamException(ErrorCode.INVALID_RATING);
        }

        double currentRating = this.rating;
        int count = this.tastingNoteCount;

        // 새로운 평점을 추가한 후 총점 계산
        double totalScore = count * currentRating + newRating;
        this.tastingNoteCount = count + 1;

        // 새로운 평균 계산
        this.rating = totalScore / this.tastingNoteCount;
    }

    public void updateRating(Double existingRating, Double newRating) {
        if (newRating == null || newRating < 0 || newRating > 5) {
            throw new InvalidParamException(ErrorCode.INVALID_RATING);
        }
        double currentRating = this.rating;
        int count = this.tastingNoteCount;
        double totalScore = count * currentRating - existingRating + newRating;
        this.rating = totalScore / this.tastingNoteCount;
    }

    public void removeRating(Double existingRating) {
        double currentRating = this.rating;
        int count = this.tastingNoteCount;
        double totalScore = count * currentRating - existingRating;
        this.tastingNoteCount = count - 1;
        this.rating = totalScore / this.tastingNoteCount;
    }

}
