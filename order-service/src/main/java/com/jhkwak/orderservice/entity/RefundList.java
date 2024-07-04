package com.jhkwak.orderservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "refund_list")
@Getter
@Setter
@NoArgsConstructor
public class RefundList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long productId;

    @OneToOne
    @JoinColumn(name = "order_detail_id", referencedColumnName = "id", nullable = false)
    private OrderListDetail orderListDetail;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private Character status;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate refundRequestOn;

    public RefundList(Long userId, Long productId, OrderListDetail orderListDetail, int quantity, Long price){

        this.userId = userId;
        this.productId = productId;
        this.orderListDetail = orderListDetail;
        this.quantity = quantity;
        this.price = price;
        this.status = 'S';
        this.refundRequestOn = LocalDate.now();

    }
}