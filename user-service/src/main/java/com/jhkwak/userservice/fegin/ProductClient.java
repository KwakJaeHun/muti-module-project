package com.jhkwak.userservice.fegin;

import com.jhkwak.userservice.config.FeignClientConfig;
import com.jhkwak.userservice.dto.CartRequestDto;
import com.jhkwak.userservice.dto.CartResponseDto;
import com.jhkwak.userservice.dto.WishListResponseDto;
import com.jhkwak.userservice.dto.WishRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service", url = "http://localhost:9000/product", configuration = FeignClientConfig.class)
public interface ProductClient {

    // wish-list 조회
    @GetMapping("/wish-list")
    List<WishListResponseDto> wishList();

    // wish-list 삭제
    @DeleteMapping("/wish-delete")
    List<WishListResponseDto> wishDelete(
            @RequestBody WishRequestDto wishRequestDto
    );

    // 장바구니 리스트
    @GetMapping("/cart-list")
    List<CartResponseDto> getCartList();

    // 장바구니 업데이트
    @PutMapping("/cart-update")
    List<CartResponseDto> cartUpdate(
            @RequestBody CartRequestDto cartRequestDto
    );

    // 장바구니 삭제
    @DeleteMapping("/cart-delete")
    List<CartResponseDto> cartDelete(
            @RequestBody CartRequestDto cartRequestDto
    );
}
