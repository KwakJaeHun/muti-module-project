package com.jhkwak.productservice.service;

import com.jhkwak.productservice.dto.CartRequestDto;
import com.jhkwak.productservice.dto.CartResponseDto;
import com.jhkwak.productservice.entity.Cart;
import com.jhkwak.productservice.entity.Product;
import com.jhkwak.productservice.repository.CartRepository;
import com.jhkwak.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    // cartList
    public List<CartResponseDto> getCartList(Long userId) {

        return cartRepository.findByUserId(userId).stream()
                .map(cartList -> new CartResponseDto(
                        cartList.getProduct().getId(),
                        cartList.getProduct().getName(),
                        cartList.getQuantity(),
                        cartList.getProduct().getPrice() * cartList.getQuantity(),
                        cartList.getProduct().getProductStock().getStockQuantity())
                ).toList();
    }

    // cart 등록
    @Transactional
    public List<CartResponseDto> cartAdd(Long userId, CartRequestDto cartRequestDto) {

        for (int i = 0; i < cartRequestDto.getProductId().size(); i++) {
            List<Cart> cartExist = cartExist(userId, new ArrayList<>(Arrays.asList(cartRequestDto.getProductId().get(i))));

            // 장바구니에 동일한 상품이 존재하면 수량 더해서 업데이트
            if(!cartExist.isEmpty()){
                int sumQuantity = cartExist.get(i).getQuantity() + cartRequestDto.getQuantity().get(i);
                cartExist.get(i).setQuantity(sumQuantity);
            }
            // 없으면 새로 등록
            else{
                Product product = productRepository.findById(cartRequestDto.getProductId().get(i)).orElseThrow(() -> new IllegalArgumentException("Product not found with id"));
                Cart cart = new Cart(userId, product, cartRequestDto.getQuantity().get(i));
                cartRepository.save(cart);
            }
        }

        return getCartList(userId);
    }

    // cart 업데이트
    @Transactional
    public List<CartResponseDto> cartUpdate(Long userId, CartRequestDto cartRequestDto) {
        
        List<Cart> cartExist = cartExist(userId, cartRequestDto.getProductId());
        // 장바구니에 동일한 상품이 존재하면 수량 더해서 업데이트
        if(!cartExist.isEmpty()){
            for (int i = 0; i < cartExist.size(); i++) {
                cartExist.get(i).setQuantity(cartRequestDto.getQuantity().get(i));
            }
        }

        return getCartList(userId);
    }
    
    // cart 삭제
    public List<CartResponseDto> cartDelete(Long userId, CartRequestDto cartRequestDto) {

        List<Cart> cartExist = cartExist(userId, cartRequestDto.getProductId());
        if(!cartExist.isEmpty()){

            for(Cart cart : cartExist){
                cartRepository.delete(cart);
            }
        }

        return getCartList(userId);
    }

    private Product findByProduct(Long productId){
        return productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found with id:" + productId));
    }
    
    private List<Cart> cartExist(Long userId, List<Long> productIds){

        return cartRepository.findByUserIdAndProductIdIn(userId, productIds);
    }
}
