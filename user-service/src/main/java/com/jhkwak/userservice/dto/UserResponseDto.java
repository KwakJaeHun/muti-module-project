package com.jhkwak.userservice.dto;

import com.jhkwak.userservice.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponseDto {
    private String email;
    private String name;
    private LocalDateTime createAt;

    public UserResponseDto(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
        this.createAt = user.getCreatedAt();
    }
}
