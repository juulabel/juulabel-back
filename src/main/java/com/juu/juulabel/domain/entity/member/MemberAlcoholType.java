package com.juu.juulabel.domain.entity.member;

import com.juu.juulabel.domain.base.BaseTimeEntity;
import com.juu.juulabel.domain.entity.alcohol.AlcoholType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "member_alcohol_type")
public class MemberAlcoholType extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '선호전통주 주종 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liquor_type_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '전통주 주종 고유 번호'")
    private AlcoholType alcoholType;

    public static MemberAlcoholType create(Member member, AlcoholType alcoholType) {
        return MemberAlcoholType.builder()
            .member(member)
            .alcoholType(alcoholType)
            .build();
    }

}