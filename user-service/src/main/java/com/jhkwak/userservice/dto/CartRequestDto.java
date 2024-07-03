package com.jhkwak.userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartRequestDto {
    private Long productId;
    private int quantity;
}
