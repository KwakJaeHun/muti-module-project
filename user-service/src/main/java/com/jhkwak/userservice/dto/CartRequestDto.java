package com.jhkwak.userservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class CartRequestDto {
    private List<Long> productId;
    private List<Integer> quantity;
}
