package com.jhkwak.orderservice.controller;

import com.jhkwak.orderservice.dto.product.ProductDetailResponseDto;
import com.jhkwak.orderservice.dto.product.ProductRegRequestDto;
import com.jhkwak.orderservice.dto.product.ProductResponseDto;
import com.jhkwak.orderservice.dto.product.WishRequestDto;
import com.jhkwak.orderservice.dto.user.CartRequestDto;
import com.jhkwak.orderservice.dto.user.CartResponseDto;
import com.jhkwak.orderservice.entity.product.Product;
import com.jhkwak.orderservice.security.UserDetailImpl;
import com.jhkwak.orderservice.service.product.ProductService;
import com.jhkwak.orderservice.service.user.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CartService cartService;

    @GetMapping("/list")
    public List<ProductResponseDto> productList(){
        return productService.productList();
    }

    @GetMapping("/detail/{productId}")
    public List<ProductDetailResponseDto> productDetail(@PathVariable Long productId){
        return productService.productDetail(productId);
    }

    @PostMapping("/registration")
    public Product productRegistration(@RequestBody ProductRegRequestDto productRegRequestDto){
        return productService.productRegistration(productRegRequestDto);
    }

    @PostMapping("/wish-add")
    public ResponseEntity<?> wishAdd(
            @AuthenticationPrincipal UserDetailImpl userDetail,
            @RequestBody WishRequestDto wishRequestDto
    )
    {

        productService.wishAdd(userDetail.getUser(), wishRequestDto);
        return ResponseEntity.ok().build();
    }

    // 장바구니 추가
    @PostMapping("/cart-add")
    public ResponseEntity<?> cartRegistration(
            @AuthenticationPrincipal UserDetailImpl userDetail,
            @RequestBody CartRequestDto cartRequestDto
    )
    {
        List<CartResponseDto> cartList = cartService.cartRegistration(userDetail.getId(), cartRequestDto);
        return ResponseEntity.ok(cartList);
    }
}
