package com.jhkwak.productservice.controller;

import com.jhkwak.productservice.dto.*;
import com.jhkwak.productservice.entity.Product;
import com.jhkwak.productservice.service.CartService;
import com.jhkwak.productservice.service.ProductService;
import com.jhkwak.productservice.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final WishListService wishListService;
    private final CartService cartService;

    // 상품 등록
    @PostMapping("/registration")
    public Product productRegistration(
            @RequestBody ProductRegRequestDto productRegRequestDto
    ){
        return productService.productRegistration(productRegRequestDto);
    }
    
    // 상품 전체 리스트
    @GetMapping("/list/all")
    public List<ProductResponseDto> productListAll(){
        return productService.productListAll();
    }

    // product_id 한개로 한개의 상품 조회
    @PostMapping("/list/one")
    public ProductResponseDto productList(@RequestBody ProductRequestDto productRequestDto){
        return productService.productList(productRequestDto);
    }

    // product_id 여러개로 여러개의 list 조회
    @PostMapping("/list/many")
    public List<ProductResponseDto> productListMany(@RequestBody ProductListRequestDto productListRequestDto){
        return productService.productListMany(productListRequestDto);
    }
    
    // 상품 상세 정보
    @GetMapping("/detail/{productId}")
    public List<ProductDetailResponseDto> productDetail(@PathVariable Long productId){
        return productService.productDetail(productId);
    }
    
    // wish-list 조회
    @GetMapping("/wish-list")
    public ResponseEntity<?> wishList(
            @RequestHeader("X-Authenticated-User") String userId
    )
    {
        List<WishResponseDto> wishList = wishListService.getWishList(Long.parseLong(userId));
        return ResponseEntity.ok(wishList);
    }
    
    // wish-list 추가
    @PostMapping("/wish-add")
    public ResponseEntity<?> wishAdd(
            @RequestHeader("X-Authenticated-User") String userId,
            @RequestBody WishRequestDto wishRequestDto
    )
    {
        wishListService.wishAdd(Long.parseLong(userId), wishRequestDto);
        return ResponseEntity.ok("Wish Add Success");
    }

    // wish-list 삭제
    @DeleteMapping("/wish-delete")
    public ResponseEntity<?> wishDelte(
            @RequestHeader("X-Authenticated-User") String userId,
            @RequestBody WishRequestDto wishRequestDto
    )
    {
        List<WishResponseDto> wishList = wishListService.wishDelete(Long.parseLong(userId), wishRequestDto);
        return ResponseEntity.ok(wishList);
    }

    // 장바구니 리스트
    @GetMapping("/cart-list")
    public ResponseEntity<?> getCartList(
            @RequestHeader("X-Authenticated-User") String userId
    )
    {
        List<CartResponseDto> cartList = cartService.getCartList(Long.parseLong(userId));
        return ResponseEntity.ok(cartList);
    }

    // 장바구니 추가
    @PostMapping("/cart-add")
    public ResponseEntity<?> cartAdd(
            @RequestHeader("X-Authenticated-User") String userId,
            @RequestBody CartRequestDto cartRequestDto
    )
    {
        List<CartResponseDto> cartList = cartService.cartAdd(Long.parseLong(userId), cartRequestDto);
        return ResponseEntity.ok(cartList);
    }

    // 장바구니 업데이트
    @PutMapping("/cart-update")
    public ResponseEntity<?> cartUpdate(
            @RequestHeader("X-Authenticated-User") String userId,
            @RequestBody CartRequestDto cartRequestDto
    )
    {
        List<CartResponseDto> cartList = cartService.cartUpdate(Long.parseLong(userId), cartRequestDto);
        return ResponseEntity.ok(cartList);
    }

    // 장바구니 삭제
    @DeleteMapping("/cart-delete")
    public ResponseEntity<?> cartDelete(
            @RequestHeader("X-Authenticated-User") String userId,
            @RequestBody CartRequestDto cartRequestDto
    )
    {
        List<CartResponseDto> cartList = cartService.cartDelete(Long.parseLong(userId), cartRequestDto);
        return ResponseEntity.ok(cartList);
    }

    // 재고수량 업데이트
    @PutMapping("/stock-update")
    public ResponseEntity<?> stockUpdate(
            @RequestBody CartRequestDto cartRequestDto
    )
    {
        boolean updateSuccess = productService.stockUpdate(cartRequestDto);
        if(updateSuccess){
            return ResponseEntity.ok("Success");
        }
        else{
            return ResponseEntity.ok("Failed");
        }
    }
}
