package com.jhkwak.orderservice.service;

import com.jhkwak.orderservice.dto.*;
import com.jhkwak.orderservice.entity.OrderList;
import com.jhkwak.orderservice.entity.OrderListDetail;
import com.jhkwak.orderservice.entity.RefundList;
import com.jhkwak.orderservice.feign.ProductClient;
import com.jhkwak.orderservice.feign.UserClient;
import com.jhkwak.orderservice.repository.OrderListDetailRepository;
import com.jhkwak.orderservice.repository.OrderListRepository;
import com.jhkwak.orderservice.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "동시성 테스트")
public class OrderService {

    private final OrderListRepository orderListRepository;
    private final OrderListDetailRepository orderListDetailRepository;
    private final RefundRepository refundRepository;
    private final UserClient userClient;
    private final ProductClient productClient;
    private final Random random = new Random();

    // 주문 정보 list
    public List<OrderListResponseDto> orderList(Long userId) {

        List<OrderList> orderLists = orderListRepository.findByUserId(userId);

        return orderLists.stream()
                .map(orderList -> {
                        OrderListResponseDto dto = new OrderListResponseDto();
                        dto.setOrderListId(orderList.getId());
                        dto.setStatus(orderList.getStatus());
                        dto.setCreatedAt(orderList.getCreatedAt());

                        ArrayList<Map<String, Object>> orderListDetails = new ArrayList<>();
                        orderList.getOrderListDetails().stream()
                                .forEach(orderListDetail -> {
                                    Map<String, Object> detailMap = new LinkedHashMap<>();

                                    // 상품 정보
                                    detailMap.put("orderListDetailId", orderListDetail.getId());
                                    detailMap.put("productId", orderListDetail.getProductId());
                                    ProductResponseDto productResponseDto = productClient.productList(new ProductRequestDto(orderListDetail.getProductId()));
                                    detailMap.put("productName", productResponseDto.getName());
                                    detailMap.put("productThumbnailImage", productResponseDto.getThumbnailImage());
                                    detailMap.put("productSoldOutStatus", productResponseDto.getSoldOutStatus());
                                    detailMap.put("productNewStatus", productResponseDto.getNewStatus());
                                    detailMap.put("productBestStatus", productResponseDto.getBestStatus());
                                    // 주문 상세 정보
                                    detailMap.put("quantity", orderListDetail.getQuantity());
                                    detailMap.put("price", orderListDetail.getPrice());
                                    detailMap.put("status", orderListDetail.getStatus());
                                    detailMap.put("deliveryStatus", orderListDetail.getDeliveryStatus());
                                    detailMap.put("creatAt", orderListDetail.getCreatedAt());
                                    orderListDetails.add(detailMap);
                                });

                        dto.setOrderListDetails(orderListDetails);

                    return dto;
                })
                .toList();
    }

    // 주문 진행 페이지
    public OrderCheckOutListResponseDto checkOutPage(String accessToken, List<String> items) {

        // 고객 정보 얻어오기
        UserResponseDto userResponseDto = userClient.userInfo();

        // 파라미터로 넘어온 주문 상품과 수량
        Map<Long, Integer> productIdQuantityMap = new HashMap<>();
        for (String item : items) {
            String[] parts = item.split(":");
            Long productId = Long.parseLong(parts[0]);
            Integer quantity = Integer.parseInt(parts[1]);
            productIdQuantityMap.put(productId, quantity);
        }

        // 상품 정보 가져오기
        ProductListRequestDto productListRequestDto = new ProductListRequestDto(new ArrayList<>(productIdQuantityMap.keySet()));
        List<ProductResponseDto> products = productClient.productListMany(productListRequestDto);


        // 주문 상풍 정보 매핑
        List<OrderProductResponseDto> orderProductResponseDto  =  products.stream()
                .map(product -> {
                    OrderProductResponseDto dto = new OrderProductResponseDto();
                    dto.setProductId(product.getProductId());
                    dto.setName(product.getName());
                    dto.setPrice(product.getPrice() * productIdQuantityMap.get(product.getProductId()));
                    dto.setQuantity(productIdQuantityMap.get(product.getProductId()));
                    dto.setThumbnailImage(product.getThumbnailImage());
                    return dto;
                })
                .toList();

        // 주문 정보 return
        return new OrderCheckOutListResponseDto(userResponseDto, orderProductResponseDto);
    }
    
    // 주문 진행
    public boolean checkOut(String accessToken, Long userId, List<OrderListRequestDto> orderListRequestDto) {

        // 20% 실패처리
        if (random.nextDouble() < 0.2) {
            log.info("결제 진입시 실패");
            return false;
        }

        // 주문된 장바구니 데이터 삭제 및 재고 반영 데이터 생성
        List<Long> productIds = new ArrayList<>();
        List<Integer> productQuantity = new ArrayList<>();
        for(OrderListRequestDto dto : orderListRequestDto){
            productIds.add(dto.getProductId());
            productQuantity.add(dto.getQuantity());
        }

        CartAndStockRequestDto stockMinusUpdateAndCartDelete = new CartAndStockRequestDto("minus", productIds, productQuantity);

        // 재고 확인 및 재고 감소
        String purchaseSuccess = productClient.stockUpdate(stockMinusUpdateAndCartDelete);
        if(purchaseSuccess.equals("Failed")){
            return false;
        }

        // 20% 실패처리
        if (random.nextDouble() < 0.2) {
            log.info("결제 진행 중 실패");
            CartAndStockRequestDto stockPlusUpdate = new CartAndStockRequestDto("plus", productIds, productQuantity);
            productClient.stockUpdate(stockPlusUpdate);
            return false;
        }

        // 주문 상세 만들기
        List<OrderListDetail> orderListDetails  =  orderListRequestDto.stream()
                .map(orderListData -> {
                    OrderListDetail orderListDetail = new OrderListDetail(
                            orderListData.getProductId(),
                            orderListData.getQuantity(),
                            orderListData.getPrice()
                    );
                    return orderListDetail;
                })
                .toList();


        // 주문 정보 생성
        OrderList orderList = new OrderList(userId, orderListDetails, 'Y');
        try {
            orderListRepository.save(orderList);
        } catch (Exception e) {
            log.error("Order save failed", e);
            return false;
        }

        orderFinishCartDelete(stockMinusUpdateAndCartDelete);
        
        return true;
    }

    @Async
    public void orderFinishCartDelete(CartAndStockRequestDto stockMinusUpdateAndCartDelete){
        productClient.cartDelete(stockMinusUpdateAndCartDelete);
    }
    
    // 주문 취소
    @Transactional
    public boolean orderCancel(String accessToken, Long userId, OrderCancelRefundRequestDto cancelDto) {
        OrderList orderList = orderListRepository.findByUserIdAndId(userId, cancelDto.getOrderListId());
        
        int cancelCount = 0;

        for(OrderListDetail orderListDetail : orderList.getOrderListDetails()){

            // 취소 품목 카운팅
            if(orderListDetail.getStatus() == 'C'){
                cancelCount++;
            }

            // 상세 아이디가 같고 배송 전 상태이면 취소 진행
            if(Objects.equals(orderListDetail.getId(), cancelDto.getOrderListDetailId()) && orderListDetail.getDeliveryStatus() == 'S'){
                orderListDetail.setStatus('C');
                orderListDetail.setDeliveryStatus('C');
                cancelCount++;

                // 재고 복구
                CartAndStockRequestDto cartAndStockRequestDto = new CartAndStockRequestDto("plus", new ArrayList<>(Arrays.asList(orderListDetail.getProductId())), new ArrayList<>(Arrays.asList(orderListDetail.getQuantity())));
                productClient.stockUpdate(cartAndStockRequestDto);
            }
            else if(Objects.equals(orderListDetail.getId(), cancelDto.getOrderListDetailId()) && orderListDetail.getDeliveryStatus() != 'S'){
                return false;
            }
        }

        // orderList안의 orderListDetail이 전부 취소 되었으면 orderList 상태값을 C로 변경
        if(orderList.getOrderListDetails().size() == cancelCount){
            orderList.setStatus('C');
        }

        return true;
    }
    
    // 주문 반품
    @Transactional
    public boolean orderRefund(String accessToken, Long userId, OrderCancelRefundRequestDto refundDto) {

        OrderListDetail orderListDetail = orderListDetailRepository.findById(refundDto.getOrderListDetailId()).get();

        LocalDate now = LocalDate.now();

        // 상세 아이디가 같을때
        if(Objects.equals(orderListDetail.getId(), refundDto.getOrderListDetailId())) {
            // 배송이 완료 되었고 오늘이 배송 완료일 + 2일 보다 작으면 반품 진행 상태로 업데이트
            if(orderListDetail.getDeliveryStatus() == 'Y' && now.isBefore(orderListDetail.getDeliveryCompleteOn().plusDays(2))){
                orderListDetail.setStatus('P');
                orderListDetail.setDeliveryStatus('P');

                // 반품 목록 등록
                RefundList refundList = new RefundList(userId, orderListDetail.getProductId(), orderListDetail, orderListDetail.getQuantity(), orderListDetail.getPrice());

                refundRepository.save(refundList);
            }
            else{
                return false;
            }
        }

        return true;
    }
    
    // 자정에 배송 상태 없데이트, 반품 신청 후 하루가 지났을때 반품 완료 업데이트 , 재고 복구 스케줄러
    @Transactional
    public void deliveryStatusUpdate() {

        LocalDate now = LocalDate.now();

        // 접수인 상품 배송 중으로 업데이트
        List<OrderListDetail> delivery = orderListDetailRepository
                .findByOrderConfirmOnBeforeAndDeliveryStatus(now, 'S');

        for (OrderListDetail detail : delivery) {
            detail.setDeliveryStatus('D');
        }

        // 배송 중인 상품 배송 완료로 업데이트
        List<OrderListDetail> deliveryComplete = orderListDetailRepository
                .findByOrderConfirmOnBeforeAndDeliveryStatus(now.minusDays(1), 'D');

        for (OrderListDetail detail : deliveryComplete) {
            detail.setDeliveryStatus('Y');
            detail.setDeliveryCompleteOn(now);
        }

        // 반품 신청 후 하루가 지났을때 반품 완료 업데이트 , 재고 복구 스케줄러
        List<RefundList> refundListComplete = refundRepository.findByRefundRequestOnBeforeAndStatus(now, 'S');

        for (RefundList refundListDetail : refundListComplete) {

            // 반품 테이블 반품 완료 처리
            refundListDetail.setStatus('Y');

            // 주문 상세 테이블 반품 완료 처리
            OrderListDetail orderListDetail = orderListDetailRepository.findById(refundListDetail.getOrderListDetail().getId()).get();
            orderListDetail.setDeliveryStatus('R');
            orderListDetail.setStatus('R');

            // 재고 복구
            CartAndStockRequestDto cartAndStockRequestDto = new CartAndStockRequestDto("plus", new ArrayList<>(Arrays.asList(orderListDetail.getProductId())), new ArrayList<>(Arrays.asList(orderListDetail.getQuantity())));
            productClient.stockUpdate(cartAndStockRequestDto);

            List<OrderListDetail> orderListDetails = orderListDetailRepository.findByOrderListId(orderListDetail.getOrderList().getId());

            // 주문 리스트 테이블 전체 반품 완료되었을때 반품 완료 처리
            boolean refundStatus = true;
            Long orderListId = 0L;
            for(OrderListDetail orderListDetailAll : orderListDetails){
                orderListId = orderListDetailAll.getOrderList().getId();
                if(orderListDetailAll.getStatus() != 'R'){
                    refundStatus = false;
                }
            }

            if(refundStatus){
                OrderList orderList = orderListRepository.findById(orderListId).get();
                orderList.setStatus('R');
            }
        }
    }
}
