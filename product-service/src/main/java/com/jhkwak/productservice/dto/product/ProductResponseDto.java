package com.jhkwak.productservice.dto.product;

import com.jhkwak.productservice.entity.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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

    public ProductResponseDto(Product product){
        this.productId = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.thumbnailImage = product.getThumbnailImage();
        this.soldOutStatus = product.getSoldOutStatus();
        this.newStatus = product.getNewStatus();
        this.bestStatus = product.getBestStatus();

    }
}
