package com.jhkwak.productservice.service.order;

import com.jhkwak.productservice.dto.order.*;
import com.jhkwak.productservice.dto.user.UserResponseDto;
import com.jhkwak.productservice.entity.Response;
import com.jhkwak.productservice.entity.ResponseCode;
import com.jhkwak.productservice.entity.order.OrderList;
import com.jhkwak.productservice.entity.order.OrderListDetail;
import com.jhkwak.productservice.entity.product.Product;
import com.jhkwak.productservice.entity.product.ProductStock;
import com.jhkwak.productservice.entity.refund.RefundList;
import com.jhkwak.productservice.entity.user.User;
import com.jhkwak.productservice.repository.order.OrderListDetailRepository;
import com.jhkwak.productservice.repository.order.OrderListRepository;
import com.jhkwak.productservice.repository.product.ProductRepository;
import com.jhkwak.productservice.repository.product.ProductStockRepository;
import com.jhkwak.productservice.repository.refund.RefundRepository;
import com.jhkwak.productservice.repository.user.CartRepository;
import com.jhkwak.productservice.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductStockRepository productStockRepository;
    private final OrderListRepository orderListRepository;
    private final CartRepository cartRepository;
    private final OrderListDetailRepository orderListDetailRepository;
    private final RefundRepository refundRepository;

    // 주문 정보 list
    public List<OrderListResponseDto> orderList(Long userId) {

        List<OrderList> orderLists = orderListRepository.findByUserId(userId);

        return orderLists.stream()
                .map(orderList -> {
                    OrderListResponseDto dto = new OrderListResponseDto();
                    dto.setOrderListId(orderList.getId());
                    dto.setStatus(orderList.getStatus());
                    dto.setCreatedAt(orderList.getCreatedAt());
                    dto.setOrderListDetails(orderList.getOrderListDetails());

                    // JsonIgnore Annotation 으로 대체
//                        ArrayList<Map<String, Object>> orderListDetails = new ArrayList<>();
//                        orderList.getOrderListDetails().stream()
//                                .forEach(orderListDetail -> {
//                                    Map<String, Object> detailMap = new HashMap<>();
//                                    detailMap.put("orderListDetailId", orderListDetail.getId());
//                                    detailMap.put("productId", orderListDetail.getProduct().getId());
//                                    detailMap.put("quantity", orderListDetail.getQuantity());
//                                    detailMap.put("price", orderListDetail.getPrice());
//                                    detailMap.put("status", orderListDetail.getStatus());
//                                    detailMap.put("deliveryStatus", orderListDetail.getDeliveryStatus());
//                                    detailMap.put("creatAt", orderListDetail.getCreatedAt());
//                                    orderListDetails.add(detailMap);
//                                });
//
//                        dto.setOrderListDetails(orderListDetails);

                    return dto;
                })
                .toList();
    }

    // 주문 진행 페이지
    public OrderCheckOutListResponseDto checkOutPage(Long userId, List<String> items) {
        User user = userRepository.findById(userId).get();
        
        // 고객 정보 저장
        UserResponseDto userResponseDto = new UserResponseDto(user);

        // 파라미터로 넘어온 주문 상품과 수량
        Map<Long, Integer> productIdQuantityMap = new HashMap<>();
        for (String item : items) {
            String[] parts = item.split(":");
            Long productId = Long.parseLong(parts[0]);
            Integer quantity = Integer.parseInt(parts[1]);
            productIdQuantityMap.put(productId, quantity);
        }
        
        // 상품 정보 가져오기
        List<Long> productIds = new ArrayList<>(productIdQuantityMap.keySet());
        List<Product> products = productRepository.findByIdIn(productIds);
        
        // 주문 상풍 정보 매핑
        List<OrderProductResponseDto> orderProductResponseDto  =  products.stream()
                .map(product -> {
                    OrderProductResponseDto dto = new OrderProductResponseDto();
                    dto.setProductId(product.getId());
                    dto.setName(product.getName());
                    dto.setPrice(product.getPrice() * productIdQuantityMap.get(product.getId()));
                    dto.setQuantity(productIdQuantityMap.get(product.getId()));
                    dto.setThumbnailImage(product.getThumbnailImage());
                    return dto;
                })
                .toList();

        // 주문 정보 return
        return new OrderCheckOutListResponseDto(userResponseDto, orderProductResponseDto);
    }
    
    // 주문 진행
    @Transactional
    public void checkOut(Long userId, List<OrderListRequestDto> orderListRequestDto) {
        
        // 고객 정보
        User user = userRepository.findById(userId).get();
        
        // 주문 상세 만들기
        List<OrderListDetail> orderListDetails  =  orderListRequestDto.stream()
                .map(orderListData -> {
                    Product product = productRepository.findById(orderListData.getProductId()).get();
                    OrderListDetail orderListDetail = new OrderListDetail(
                            product,
                            orderListData.getQuantity(),
                            orderListData.getPrice()
                    );
                    return orderListDetail;
                })
                .toList();


        // 주문 정보 생성
        OrderList orderList = new OrderList(user, orderListDetails, 'Y');
        orderListRepository.save(orderList);
        
        // 아래 for문에 통합
//        List<Long> productIds = orderListRequestDto.stream()
//                .map(OrderListRequestDto::getProductId)
//                .toList();
//
//        cartRepository.deleteByUserIdAndProductIdIn(userId, productIds);

        
        for(OrderListRequestDto dto : orderListRequestDto){
            // 주문된 상품은 장바구니에서 삭제
            cartRepository.deleteByUserIdAndProductId(userId, dto.getProductId());
            
            // 재고 수량 업데이트
            ProductStock productStock = productStockRepository.findById(dto.getProductId()).get();
            if(productStock.getStockQuantity() - dto.getQuantity() > 0){
                productStock.setStockQuantity(productStock.getStockQuantity() - dto.getQuantity());
            }
            else{
                productStock.setStockQuantity(0);
            }
        }
    }
    
    // 주문 취소
    @Transactional
    public boolean orderCancel(Long userId, OrderCancelRefundRequestDto cancelDto) {
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
                ProductStock productStock = productStockRepository.findById(orderListDetail.getProduct().getId()).get();
                productStock.setStockQuantity(productStock.getStockQuantity() + orderListDetail.getQuantity());
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
    public boolean orderRefund(Long userId, OrderCancelRefundRequestDto refundDto) {

        OrderListDetail orderListDetail = orderListDetailRepository.findById(refundDto.getOrderListDetailId()).get();

        LocalDate now = LocalDate.now();

        // 상세 아이디가 같을때
        if(Objects.equals(orderListDetail.getId(), refundDto.getOrderListDetailId())) {
            // 배송이 완료 되었고 오늘이 배송 완료일 + 2일 보다 작으면 반품 진행 상태로 업데이트
            if(orderListDetail.getDeliveryStatus() == 'Y' && now.isBefore(orderListDetail.getDeliveryCompleteOn().plusDays(2))){
                orderListDetail.setStatus('P');
                orderListDetail.setDeliveryStatus('P');

                // 반품 목록 등록
                User user = userRepository.findById(userId).get();
                RefundList refundList = new RefundList(user, orderListDetail.getProduct(), orderListDetail, orderListDetail.getQuantity(), orderListDetail.getPrice());

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
        List<RefundList> refundListComplete = refundRepository
                .findByRefundRequestOnBeforeAndStatus(now, 'S');
        
        for (RefundList refundListDetail : refundListComplete) {
            
            // 반품 테이블 반품 완료 처리
            refundListDetail.setStatus('Y');
            
            // 주문 상세 테이블 반품 완료 처리
            OrderListDetail orderListDetail = orderListDetailRepository.findById(refundListDetail.getOrderListDetail().getId()).get();
            orderListDetail.setDeliveryStatus('R');
            orderListDetail.setStatus('R');

            // 재고 복구
            ProductStock productStock = productStockRepository.findById(orderListDetail.getProduct().getId()).get();
            productStock.setStockQuantity(productStock.getStockQuantity() + orderListDetail.getQuantity());

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
