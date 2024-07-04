package com.jhkwak.orderservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductRequestDto {
    private Long productId;

    public ProductRequestDto(Long productId){
        this.productId = productId;
    }
}
