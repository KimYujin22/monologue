package com.nerdy.monologue.member.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMemberRequest {
    private String name;
    private String newPassword;
    private String imageUrl;
}
