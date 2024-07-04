package com.jhkwak.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CartAndStockRequestDto {
    private String type;
    private List<Long> productId;
    private List<Integer> quantity;
}

