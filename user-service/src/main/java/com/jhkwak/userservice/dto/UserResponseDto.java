package com.jhkwak.userservice.dto;

import com.jhkwak.userservice.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponseDto {
    private String name;
    private String address;
    private String phone;
    private String email;
    private LocalDateTime createAt;

    public UserResponseDto(User user) {
        this.name = user.getName();
        this.address = user.getAddress();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.createAt = user.getCreatedAt();
    }
}
