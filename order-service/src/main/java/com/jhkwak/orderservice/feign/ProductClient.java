package com.jhkwak.orderservice.feign;

import com.jhkwak.orderservice.config.FeignClientConfig;
import com.jhkwak.orderservice.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "product-service", url = "http://api-gateway:9000/product", configuration = FeignClientConfig.class)
//@FeignClient(name = "product-service", url = "http://localhost:9000/product", configuration = FeignClientConfig.class)
public interface ProductClient {
    
    // product_id 한개로 한개의 상품 조회
    @PostMapping("/list")
    ProductResponseDto productList(@RequestBody ProductRequestDto productRequestDto);

    // product_id 여러개로 여러개의 list 조회
    @PostMapping("/list/many")
    List<ProductResponseDto> productListMany(@RequestBody ProductListRequestDto productListRequestDto);

    // 장바구니 삭제
    @DeleteMapping("/cart-delete")
    void cartDelete(@RequestBody CartAndStockRequestDto cartAndStockRequestDto);

    // 재고 업데이트
    @PutMapping("/stock-update")
    void stockUpdate(@RequestBody CartAndStockRequestDto cartAndStockRequestDto);

}
