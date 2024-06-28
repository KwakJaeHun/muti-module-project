package com.jhkwak.orderservice.repository.order;

import com.jhkwak.orderservice.entity.order.OrderListDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderListDetailRepository extends JpaRepository<OrderListDetail, Long> {

    List<OrderListDetail> findByOrderConfirmOnBeforeAndDeliveryStatus(LocalDate now, char s);

    List<OrderListDetail> findByOrderListId(Long orderListId);
}
