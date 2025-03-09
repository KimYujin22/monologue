package com.nerdy.monologue.member.domain.entity;

import com.nerdy.monologue.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "member")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 기본 키 자동 증가
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String imageUrl;

    // 리프레시 토큰을 저장할 필드
    @Column(nullable = true)
    private String refreshToken;

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void updateName(String newName) {
        if (!this.name.equals(newName)) {
            this.name = newName;
        }
    }

    public void updateCharacterImageUrl(String newImageUrl) {
        if (!this.imageUrl.equals(newImageUrl)) {
            this.imageUrl = newImageUrl;
        }
    }

    public void updateRefreshToken(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
    }
}

