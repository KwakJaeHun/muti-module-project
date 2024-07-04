package com.jhkwak.orderservice.dto;

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
}
