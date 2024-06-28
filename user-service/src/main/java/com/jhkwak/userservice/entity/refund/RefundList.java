package com.jhkwak.userservice.entity.refund;

import com.jhkwak.userservice.entity.order.OrderListDetail;
import com.jhkwak.userservice.entity.product.Product;
import com.jhkwak.userservice.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "refund_list")
@Getter @Setter
@NoArgsConstructor
public class RefundList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;

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

    public RefundList(User user, Product product, OrderListDetail orderListDetail, int quantity, Long price){

        this.user = user;
        this.product = product;
        this.orderListDetail = orderListDetail;
        this.quantity = quantity;
        this.price = price;
        this.status = 'S';
        this.refundRequestOn = LocalDate.now();

    }
}
