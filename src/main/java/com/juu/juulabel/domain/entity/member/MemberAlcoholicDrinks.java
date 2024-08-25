package com.juu.juulabel.domain.entity.member;

import com.juu.juulabel.domain.base.BaseCreatedTimeEntity;
import com.juu.juulabel.domain.entity.alcohol.AlcoholicDrinks;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "member_alcoholic_drinks")
public class MemberAlcoholicDrinks extends BaseCreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '선호전통주 주종 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alcoholic_drinks_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '전통주 고유 번호'")
    private AlcoholicDrinks alcoholicDrinks;

    public static MemberAlcoholicDrinks create(Member member, AlcoholicDrinks alcoholicDrinks) {
        return MemberAlcoholicDrinks.builder()
            .member(member)
            .alcoholicDrinks(alcoholicDrinks)
            .build();
    }

}