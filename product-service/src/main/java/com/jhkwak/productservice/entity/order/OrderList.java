package com.jhkwak.productservice.entity.order;

import com.jhkwak.productservice.entity.TimeStamp;
import com.jhkwak.productservice.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
@Table(name = "order_list")
@NoArgsConstructor
public class OrderList extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "orderList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderListDetail> orderListDetails;

    @Column(nullable = false)
    private Character status;

    public OrderList(User user, List<OrderListDetail> orderListDetails, Character status){
        this.user = user;
        this.status = status;
        this.orderListDetails = orderListDetails;
        for (OrderListDetail detail : orderListDetails) {
            detail.setOrderList(this);
        }
    }
}
