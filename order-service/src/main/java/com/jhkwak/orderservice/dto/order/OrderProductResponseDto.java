package com.jhkwak.orderservice.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderProductResponseDto {
    private Long productId;
    private String name;
    private Long price;
    private int quantity;
    private String thumbnailImage;
}
