package com.jhkwak.orderservice.controller;

import com.jhkwak.orderservice.dto.OrderCancelRefundRequestDto;
import com.jhkwak.orderservice.dto.OrderCheckOutListResponseDto;
import com.jhkwak.orderservice.dto.OrderListRequestDto;
import com.jhkwak.orderservice.dto.OrderListResponseDto;
import com.jhkwak.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문리스트
    @GetMapping("/order-list")
    public ResponseEntity<?> orderList(
            @RequestHeader("X-Authenticated-User") String userId
    )
    {
        List<OrderListResponseDto> orderListResponseDto = orderService.orderList(Long.parseLong(userId));
        return ResponseEntity.ok(orderListResponseDto);
    }
    
    // 주문 진행 페이지
     @GetMapping("/check-out")
     public ResponseEntity<?> checkOutPage(
             @RequestHeader("Authorization") String accessToken,
             @RequestParam("item[]") List<String> items
     )
     {
         OrderCheckOutListResponseDto orderCheckOutListResponseDto = orderService.checkOutPage(accessToken, items);
         return ResponseEntity.ok(orderCheckOutListResponseDto);
     }
     
     // 주문 진행
    @PostMapping("/check-out")
    public ResponseEntity<?> checkOut(
            @RequestHeader("Authorization") String accessToken,
            @RequestHeader("X-Authenticated-User") String userId,
            @RequestBody List<OrderListRequestDto> orderListRequestDto
    )
    {
        orderService.checkOut(accessToken, Long.parseLong(userId), orderListRequestDto);
        return ResponseEntity.ok().build();
    }

    // 주문 취소
    @PutMapping("/cancel")
    public ResponseEntity<?> orderCancel(
            @RequestHeader("Authorization") String accessToken,
            @RequestHeader("X-Authenticated-User") String userId,
            @RequestBody OrderCancelRefundRequestDto orderCancelRefundRequestDto
            )
    {
        boolean isCanceled = orderService.orderCancel(accessToken, Long.parseLong(userId), orderCancelRefundRequestDto);
        if(isCanceled){
            return new ResponseEntity<>("Cancel Success", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Cancel Failed", HttpStatus.BAD_REQUEST);
        }
    }

    // 반품 신청
    @PutMapping("/refund")
    public ResponseEntity<?> orderRefund(
            @RequestHeader("Authorization") String accessToken,
            @RequestHeader("X-Authenticated-User") String userId,
            @RequestBody OrderCancelRefundRequestDto orderCancelRefundRequestDto
    )
    {
        boolean isRefunded = orderService.orderRefund(accessToken, Long.parseLong(userId), orderCancelRefundRequestDto);
        if(isRefunded){
            return ResponseEntity.ok("Refund Success");
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Products that have been shipped more than 2 days after delivery cannot be refund");
        }
    }
}
