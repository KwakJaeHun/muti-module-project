package com.jhkwak.orderservice.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jhkwak.orderservice.entity.TimeStamp;
import com.jhkwak.orderservice.entity.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Getter @Setter
@Table(name = "order_list_detail")
@NoArgsConstructor
public class OrderListDetail extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_list_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private OrderList orderList;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private Long price;

    // Y : 주문완료, C : 취소,  P : 반품 진행 중, R : 반품 완료
    @Column(nullable = false)
    private Character status;

    // S : 접수,  D : 배송중, Y : 배송완료
    @Column(nullable = false)
    private Character deliveryStatus;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate orderConfirmOn;

    @Temporal(TemporalType.DATE)
    private LocalDate deliveryCompleteOn;

    public OrderListDetail(Product product, int quantity, Long price) {
        this.product = product;
        this.price = price;
        this.quantity = quantity;
        this.status = 'Y';
        this.deliveryStatus = 'S';
        this.orderConfirmOn = LocalDate.now();
    }
}
