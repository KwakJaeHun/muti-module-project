package com.jhkwak.productservice.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
}
