package com.jhkwak.productservice.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InfoUpdateRequestDto {
    private String address;
    private String phone;
    private String currentPassword;
    private String updatePassword;
}
