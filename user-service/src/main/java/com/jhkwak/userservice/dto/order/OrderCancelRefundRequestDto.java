package com.jhkwak.userservice.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderCancelRefundRequestDto {
    private Long orderListId;
    private Long orderListDetailId;
}
