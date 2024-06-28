package com.jhkwak.userservice.repository.order;

import com.jhkwak.userservice.entity.order.OrderList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderListRepository extends JpaRepository<OrderList, Long> {
    List<OrderList> findByUserId(Long userId);

    OrderList findByUserIdAndId(Long userId, Long orderListId);
}
