package com.jhkwak.orderservice.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.Lint;

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
}
