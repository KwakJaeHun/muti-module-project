package com.jhkwak.orderservice.dto.product;


import com.jhkwak.orderservice.entity.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductDetailResponseDto {
    private String name;
    private Long price;
    private String thumbnailImage;
    private String description;
    private String descriptionImage;
    private Character soldOutStatus;
    private Character newStatus;
    private Character bestStatus;
    private int stockQuantity;

    public ProductDetailResponseDto(Product product) {
        this.name = product.getName();
        this.price = product.getPrice();
        this.thumbnailImage = product.getThumbnailImage();
        this.description = product.getDescription();
        this.descriptionImage = product.getDescriptionImage();
        this.soldOutStatus = product.getSoldOutStatus();
        this.newStatus = product.getNewStatus();
        this.bestStatus = product.getBestStatus();
        this.stockQuantity = product.getProductStock() != null ? product.getProductStock().getStockQuantity() : 0;
    }
}
