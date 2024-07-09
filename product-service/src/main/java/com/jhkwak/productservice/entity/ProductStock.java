package com.jhkwak.productservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "product_stock_quantity")
@NoArgsConstructor
public class ProductStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @JsonIgnore
    private Product product;

    @Column
    private int stockQuantity;

    public ProductStock(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void decrease(int quantity) {
        if ((stockQuantity - quantity) < 0) {
            throw new IllegalArgumentException();
        }
        stockQuantity -= quantity;
    }

    public void increase(int quantity) {
        if ((stockQuantity - quantity) < 0) {
            throw new IllegalArgumentException();
        }
        stockQuantity -= quantity;
    }
}
