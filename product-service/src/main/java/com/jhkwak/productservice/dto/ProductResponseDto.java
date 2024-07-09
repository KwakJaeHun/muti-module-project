package com.jhkwak.productservice.dto;

import com.jhkwak.productservice.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ProductResponseDto {
    private Long productId;
    private String name;
    private Long price;
    private String thumbnailImage;
    private Character soldOutStatus;
    private Character newStatus;
    private Character bestStatus;
    private int stockQuantity;


    public ProductResponseDto(Product product){
        this.productId = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.thumbnailImage = product.getThumbnailImage();
        this.soldOutStatus = product.getSoldOutStatus();
        this.newStatus = product.getNewStatus();
        this.bestStatus = product.getBestStatus();
        this.stockQuantity = product.getProductStock().getStockQuantity();
    }
}
