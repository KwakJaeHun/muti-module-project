package com.jhkwak.orderservice.dto.order;

import com.jhkwak.orderservice.entity.order.OrderListDetail;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class OrderListResponseDto {
    private Long orderListId;
    private Character status;
    private LocalDateTime createdAt;
    // private List<Map<String, Object>> orderListDetails; // JsonIgnore Annotation 으로 대체
    private List<OrderListDetail> orderListDetails;
}
