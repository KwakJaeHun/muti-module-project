package com.jhkwak.orderservice.dto;

import com.jhkwak.orderservice.entity.OrderListDetail;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter @Setter
public class OrderListResponseDto {
    private Long orderListId;
    private Character status;
    private LocalDateTime createdAt;
    private List<Map<String, Object>> orderListDetails;
}
