package com.jhkwak.productservice.dto.product;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class ProductRegRequestDto {
    private String name;
    private Long price;
    private String description;
    private String descriptionImage;
    private String thumbnailImage;
    private Character soldOutStatus;
    private Character newStatus;
    private Character bestStatus;
    private int stockQuantity;
}
