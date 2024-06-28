package com.jhkwak.productservice.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderListRequestDto {

    private Long productId;
    private int quantity;
    private Long price;
}
