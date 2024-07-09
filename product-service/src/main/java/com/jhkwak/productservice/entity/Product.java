package com.jhkwak.productservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
@Table(name = "product")
@NoArgsConstructor
public class Product extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String thumbnailImage;

    // 숫자 10자리, 소수점 2자리
    @Column(nullable = false)
    private Long price;

    @Column
    private String description;

    @Column
    private String descriptionImage;

    @Column(nullable = false, length = 1)
    private Character soldOutStatus;

    @Column(nullable = false, length = 1)
    private Character newStatus;

    @Column(nullable = false, length = 1)
    private Character bestStatus;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ProductStock productStock;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishList> wishes;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cart> cartList;

    public Product
    (
        String name,
        Long price,
        String description,
        String descriptionImage,
        String thumbnailImage,
        Character soldOutStatus,
        Character newStatus,
        Character bestStatus
    )
    {
        this.name = name;
        this.price = price != null ? price : 0;
        this.description = description;
        this.descriptionImage = descriptionImage;
        this.thumbnailImage = (thumbnailImage != null && !thumbnailImage.isEmpty()) ? thumbnailImage : "http://localhost:8080/assets/images/default_thumbnail.png";
        this.soldOutStatus = (soldOutStatus != null) ? soldOutStatus : 'N';
        this.newStatus = (newStatus != null) ? newStatus : 'N';
        this.bestStatus = (bestStatus != null) ? bestStatus : 'N';
    }

    public void setProductStock(ProductStock productStock) {
        this.productStock = productStock;
        productStock.setProduct(this);
    }

    public void purchase(int quantity) {
        productStock.decrease(quantity);
    }

    public void refund(int quantity) {
        productStock.increase(quantity);
    }
}
