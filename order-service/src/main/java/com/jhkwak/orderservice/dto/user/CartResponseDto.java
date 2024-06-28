package com.jhkwak.orderservice.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class CartResponseDto {
    private Long productId;
    private String name;
    private int quantity;
    private Long price;
    private int productStock;
}
