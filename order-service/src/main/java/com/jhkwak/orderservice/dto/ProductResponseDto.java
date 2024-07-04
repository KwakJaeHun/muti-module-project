package com.jhkwak.orderservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDto {
    private Long productId;
    private String name;
    private Long price;
    private String thumbnailImage;
    private Character soldOutStatus;
    private Character newStatus;
    private Character bestStatus;
}

