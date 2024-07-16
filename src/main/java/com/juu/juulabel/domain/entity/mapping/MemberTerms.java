package com.juu.juulabel.domain.entity.mapping;

import com.juu.juulabel.domain.base.BaseTimeEntity;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.entity.terms.Terms;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
    name = "member_terms"
)
public class MemberTerms extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '약관 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '약관 고유 번호'")
    private Terms terms;

    @Column(name = "agreed_at", columnDefinition = "datetime comment '동의 일시'")
    private LocalDateTime agreedAt;

    public static MemberTerms create(Member member, Terms terms, boolean isAgreed, LocalDateTime agreedAt) {
        return MemberTerms.builder()
            .member(member)
            .terms(terms)
            .agreedAt(isAgreed ? agreedAt : null)
            .build();
    }
}