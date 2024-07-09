package com.jhkwak.productservice.service;

import com.jhkwak.productservice.dto.*;
import com.jhkwak.productservice.entity.Product;
import com.jhkwak.productservice.entity.ProductStock;
import com.jhkwak.productservice.repository.ProductRepository;
import com.jhkwak.productservice.repository.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "재고 동시성 테스트")
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductStockRepository productStockRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedissonClient redissonClient;

    private final String STOCK_PREFIX = "productStock:";

    // 상품 전체 리스트
    public List<ProductResponseDto> productListAll() {
        return productRepository.findAll().stream().map(ProductResponseDto::new).toList();
    }

    // product_id 한개로 한개의 상품 조회
    public ProductResponseDto productList(ProductRequestDto productRequestDto) {
        Product product = productRepository.findById(productRequestDto.getProductId()).orElseThrow(() -> new IllegalArgumentException("Not registered Product - Please try again"));
        return new ProductResponseDto(product);
    }

    // product_id 여러개로 여러개의 list 조회
    public List<ProductResponseDto> productListMany(ProductListRequestDto productListRequestDto) {
        return productRepository.findByIdIn(productListRequestDto.getProductIds()).stream().map(ProductResponseDto::new).toList();
    }
    
    // 상품 상세 정보
    public List<ProductDetailResponseDto> productDetail(Long productId) {
        return productRepository.findById(productId).stream().map(ProductDetailResponseDto::new).toList();
    }
        
    // 상품 등록
    public Product productRegistration(ProductRegRequestDto productRegRequestDto) {
        
        // 상품 정보 저장
        String name                 = productRegRequestDto.getName();
        Long price                  = productRegRequestDto.getPrice();
        String description          = productRegRequestDto.getDescription();
        String descriptionImage     = productRegRequestDto.getDescription();
        String thumbnailImage       = productRegRequestDto.getThumbnailImage();
        Character soldOutStatus     = productRegRequestDto.getSoldOutStatus();
        Character newStatus         = productRegRequestDto.getNewStatus();
        Character bestStatus        = productRegRequestDto.getBestStatus();

        // 상품 재고 저장
        int stockQuantity = productRegRequestDto.getStockQuantity();

        Product product = new Product(name, price, description, descriptionImage, thumbnailImage, soldOutStatus, newStatus, bestStatus);
        ProductStock productStock = new ProductStock(stockQuantity);
        product.setProductStock(productStock);
        Product saveProduct = productRepository.save(product);

        // Redis에 상품 재고 저장
        String key = STOCK_PREFIX + saveProduct.getId();
        redisTemplate.opsForValue().set(key, saveProduct.getProductStock().getStockQuantity());

        return saveProduct;

    }
    
    // 재고 업데이트 - redis
    public boolean stockUpdate(CartRequestDto cartRequestDto) {
        if(!cartRequestDto.getProductId().isEmpty()){
            for (int i = 0; i < cartRequestDto.getProductId().size(); i++) {
                Long productId = cartRequestDto.getProductId().get(i);

                // Redisson 클라이언트를 사용하여 특정 제품에 대한 분산 락을 가져옵니다.
                // "productLock:" 접두사에 productId를 추가하여 고유한 락 키를 생성합니다.
                RLock lock = redissonClient.getLock("productLock:" + productId);

                // 생성된 락을 획득합니다.
                // 이 메서드 호출은 현재 스레드가 락을 획득할 때까지 블록(block)합니다.
                // 다른 스레드가 이미 이 락을 가지고 있다면, 해당 스레드가 락을 해제할 때까지 현재 스레드는 대기하게 됩니다.
                lock.lock();

                // 이로서 동시성 문제를 해결하고 n명의 구매처리를 진행 할 수 있습니다.
                try {
                    // Redis에서 현재 재고를 가져옴
                    Integer stock = (Integer) redisTemplate.opsForValue().get(STOCK_PREFIX + productId);

                    if (stock == null) {
                        return false;
                    }
                    // 재고가 0보다 큰 경우
                    if(cartRequestDto.getType().equals("minus")) {
                        if (stock > 0) {

                            log.info("재고 감소 전 : "  + stock);

                            // 재고를 1 감소
                            redisTemplate.opsForValue().decrement(STOCK_PREFIX + productId);

                            log.info("재고 감소 후 : "  + (stock - 1));

                            return true;
                        } else {
                            log.info("재고 부족: " + productId);
                            return false;
                        }
                    }
                    else if(cartRequestDto.getType().equals("plus")){

                        log.info("재고 증가 : "  + redisTemplate.opsForValue().get(STOCK_PREFIX + productId));
                        // 재고를 1 증가
                        redisTemplate.opsForValue().increment(STOCK_PREFIX + productId);
                        return true;
                    }
                } finally {
                    // 락을 해제
                    lock.unlock();
                }
            }
        }

        return false;
    }

//    public void stockUpdate(CartRequestDto cartRequestDto) {
//        if(!cartRequestDto.getProductId().isEmpty()){
//            for (int i = 0; i < cartRequestDto.getProductId().size(); i++) {
//
//                Optional<Product> productCheck = productRepository.findById(cartRequestDto.getProductId().get(i));
//
//                if(productCheck.isPresent()){
//
//                    Product product = productCheck.get();
//
//                    // 재고를 빼야할때
//                    if(cartRequestDto.getType().equals("minus")) {
//                        product.purchase(cartRequestDto.getQuantity().get(i));
//                    }
//                    c
//                        product.refund(cartRequestDto.getQuantity().get(i));
//                    }
//                }
//            }
//        }
//    }
}
