package com.jhkwak.userservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class WishListResponseDto {
    private Long productId;
    private String productName;
    private Long productPrice;

    public WishListResponseDto(Long productId, String productName, Long productPrice) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
    }
}
