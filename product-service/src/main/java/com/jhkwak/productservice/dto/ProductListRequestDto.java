package com.jhkwak.productservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ProductListRequestDto {
    private List<Long> productIds;
}
