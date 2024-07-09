package com.jhkwak.userservice.dto;

import com.jhkwak.userservice.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDto {

    private int status;
    private String accessToken;
    private String refreshToken;
    private String message;
    private Long userId;
    private String name;
    private String email;
    private String address;
    private String phone;

    public UserResponseDto(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public UserResponseDto(User user) {
        this.userId = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.phone = user.getPhone();
    }

    public UserResponseDto(int status, String accessToken, String refreshToken, String message, User user) {
        this.status = status;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.message = message;
        this.userId = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.phone = user.getPhone();
    }

}
