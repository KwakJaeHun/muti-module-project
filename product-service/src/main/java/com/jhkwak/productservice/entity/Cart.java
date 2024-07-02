package com.jhkwak.productservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "cart")
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    public Cart(Long userId, Product product, int quantity) {
        this.userId = userId;
        this.product = product;
        this.quantity = quantity;
    }
}
