package com.jhkwak.userservice.fegin;

import com.jhkwak.userservice.config.FeignClientConfig;
import com.jhkwak.userservice.dto.OrderListResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "order-service", url = "http://api-gateway:9000/order", configuration = FeignClientConfig.class)
// @FeignClient(name = "order-service", url = "http://localhost:9000/order", configuration = FeignClientConfig.class)
public interface OrderClient {

    // 주문리스트
    @GetMapping("/order-list")
    List<OrderListResponseDto> orderList();
}
