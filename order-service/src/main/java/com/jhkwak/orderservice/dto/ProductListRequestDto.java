package com.jhkwak.orderservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class ProductListRequestDto {
    private List<Long> productIds;

    public ProductListRequestDto(List<Long> productIds){
        this.productIds = productIds;
    }
}
