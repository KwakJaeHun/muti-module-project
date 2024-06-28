package com.jhkwak.productservice.controller;

import com.jhkwak.productservice.dto.order.OrderCancelRefundRequestDto;
import com.jhkwak.productservice.dto.order.OrderCheckOutListResponseDto;
import com.jhkwak.productservice.dto.order.OrderListRequestDto;
import com.jhkwak.productservice.dto.order.OrderListResponseDto;
import com.jhkwak.productservice.security.UserDetailImpl;
import com.jhkwak.productservice.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @AuthenticationPrincipal UserDetailImpl userDetail
    )
    {
        List<OrderListResponseDto> orderListResponseDto = orderService.orderList(userDetail.getId());
        return ResponseEntity.ok(orderListResponseDto);
    }
    
    // 주문 진행 페이지
     @GetMapping("/check-out")
     public ResponseEntity<?> checkOutPage(
         @AuthenticationPrincipal UserDetailImpl userDetail,
         @RequestParam("item[]") List<String> items
     )
     {
         OrderCheckOutListResponseDto orderCheckOutListResponseDto = orderService.checkOutPage(userDetail.getId(), items);
         return ResponseEntity.ok(orderCheckOutListResponseDto);
     }
     
     // 주문 진행
    @PostMapping("/check-out")
    public ResponseEntity<?> checkOut(
        @AuthenticationPrincipal UserDetailImpl userDetail,
        @RequestBody List<OrderListRequestDto> orderListRequestDto
    )
    {
        orderService.checkOut(userDetail.getId(), orderListRequestDto);
        return ResponseEntity.ok().build();
    }

    // 주문 취소
    @PutMapping("/cancel")
    public ResponseEntity<?> orderCancel(
            @AuthenticationPrincipal UserDetailImpl userDetail,
            @RequestBody OrderCancelRefundRequestDto orderCancelRefundRequestDto
            )
    {
        boolean isCanceled = orderService.orderCancel(userDetail.getId(), orderCancelRefundRequestDto);
        if(isCanceled){
            return new ResponseEntity<>("취소 성공", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("취소 실패", HttpStatus.BAD_REQUEST);
        }
    }

    // 반품 신청
    @PutMapping("/refund")
    public ResponseEntity<?> orderRefund(
            @AuthenticationPrincipal UserDetailImpl userDetail,
            @RequestBody OrderCancelRefundRequestDto orderCancelRefundRequestDto
    )
    {
        boolean isRefunded = orderService.orderRefund(userDetail.getId(), orderCancelRefundRequestDto);
        if(isRefunded){
            return new ResponseEntity<>("반품이 접수 되었습니다.", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("배송완료 후 2일 이상된 상품은 반품이 불가합니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
