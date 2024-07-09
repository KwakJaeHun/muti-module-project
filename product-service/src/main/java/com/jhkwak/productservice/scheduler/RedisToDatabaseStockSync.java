package com.jhkwak.productservice.scheduler;

import com.jhkwak.productservice.entity.ProductStock;
import com.jhkwak.productservice.repository.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
@Slf4j(topic = "재고 동기화 스케줄러")
public class RedisToDatabaseStockSync {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ProductStockRepository productStockRepository;

    private final String STOCK_PREFIX = "productStock:";


    // 주기적으로 재고 데이터를 동기화하는 작업
    @Scheduled(fixedRate = 60000)  // 60초마다 실행 - 예제
    @Transactional
    public void syncAllStocks() {
        log.info("재고 동기화 스케줄러 실행");

        List<String> productIds = getAllProductIds();

        for (String productId : productIds) {
            syncStockToDatabase(productId);
        }
    }

    public void syncStockToDatabase(String productId) {
        // Redis에서 재고 데이터 가져오기
        Integer stock = (Integer) redisTemplate.opsForValue().get(STOCK_PREFIX + productId);
        log.info("stock : " + stock + " / productId : " + productId);

        if (stock != null) {
            // 데이터베이스에서 해당 제품을 조회
            Optional<ProductStock> productStockCheck = productStockRepository.findById(Long.parseLong(productId));

            if(productStockCheck.isPresent()){
                ProductStock productStock = productStockCheck.get();
                // 재고 데이터 업데이트
                productStock.setStockQuantity(stock);
            }

        }
    }

    private List<String> getAllProductIds() {

        return redisTemplate.keys(STOCK_PREFIX + "*").stream()
                .map(key -> key.replace(STOCK_PREFIX, ""))
                .toList();
    }
}
