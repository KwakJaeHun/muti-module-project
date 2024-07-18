package com.jhkwak.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignupRequestDto {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
}
