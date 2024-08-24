package com.juu.juulabel.domain.entity.member;

import com.juu.juulabel.api.dto.request.SignUpMemberRequest;
import com.juu.juulabel.api.dto.request.UpdateProfileRequest;
import com.juu.juulabel.domain.enums.member.MemberRole;
import com.juu.juulabel.domain.base.BaseTimeEntity;
import com.juu.juulabel.domain.enums.member.Gender;
import com.juu.juulabel.domain.enums.member.MemberStatus;
import com.juu.juulabel.domain.enums.member.Provider;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "member"
)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Long id;

    @Column(name = "email", nullable = false, unique = true, columnDefinition = "varchar(255) comment '이메일'")
    // TODO : unique에 대한 커스텀 예외 처리
    private String email;

    @Column(name = "name", columnDefinition = "varchar(45) comment '회원 이름'")
    private String name;

    @Column(name = "nickname", nullable = false, columnDefinition = "varchar(45) comment '닉네임'")
    private String nickname;

    @Column(name = "introduction", columnDefinition = "varchar(600) comment '자기소개'")
    private String introduction;

    @Column(name = "password", columnDefinition = "varchar(255) comment '비밀번호'")
    private String password;

    @Column(name = "phone", columnDefinition = "varchar(20) comment '전화번호'")
    private String phone;

    @Column(name = "profile_image", columnDefinition = "varchar(255) comment '프로필 이미지'")
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, columnDefinition = "varchar(20) comment '성별'")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false, columnDefinition = "varchar(20) comment '가입 경로'")
    private Provider provider;

    @Column(name = "provider_id", columnDefinition = "varchar(255) comment '공급 고유 번호'")
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "varchar(20) comment '회원 상태'")
    private MemberStatus status;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "varchar(20) comment '권한'")
    private MemberRole role = MemberRole.ROLE_USER;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '탈퇴 일시'")
    private LocalDateTime deletedAt;

    public static Member create(SignUpMemberRequest signUpMemberRequest) {
        return Member.builder()
            .email(signUpMemberRequest.email())
            .nickname(signUpMemberRequest.nickname())
            .gender(signUpMemberRequest.gender())
            .provider(signUpMemberRequest.provider())
            .providerId(signUpMemberRequest.providerId())
            .status(MemberStatus.ACTIVE)
            .role(MemberRole.ROLE_USER)
            .build();
    }

    public void updateProfile(UpdateProfileRequest request) {
        this.profileImage = request.profileImageUrl();
        this.nickname = request.nickname();
        this.introduction = request.introduction();
        this.gender = request.gender();
    }
}
