package com.juu.juulabel.domain.entity.member;

import com.juu.juulabel.domain.base.BaseCreatedTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
    name = "withdrawal_record"
)
public class WithdrawalRecord extends BaseCreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '탈퇴정보 고유 번호'")
    private Long id;

    @Column(name = "withdrawal_reason", nullable = false, columnDefinition = "varchar(255) comment '탈퇴 사유'")
    private String withdrawalReason;

    @Column(name = "email", nullable = false, unique = true, columnDefinition = "varchar(255) comment '탈퇴 회원 이메일'")
    private String email;

    @Column(name = "nickname", nullable = false, columnDefinition = "varchar(45) comment '탈퇴 회원 닉네임'")
    private String nickname;

    public static WithdrawalRecord create(String withdrawalReason, String email, String nickname) {
        return WithdrawalRecord.builder()
            .withdrawalReason(withdrawalReason)
            .email(email)
            .nickname(nickname)
            .build();
    }

}
