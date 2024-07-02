package com.jhkwak.productservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class WishResponseDto {
    private Long productId;
    private String productName;
    private Long productPrice;

    public WishResponseDto(Long productId, String productName, Long productPrice) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
    }
}