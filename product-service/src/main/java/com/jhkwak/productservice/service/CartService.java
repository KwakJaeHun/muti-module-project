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
        Product product = findByProduct(cartRequestDto.getProductId());

        Optional<Cart> cartExist = cartExist(userId, product.getId());
        // 장바구니에 동일한 상품이 존재하면 수량 더해서 업데이트
        if(cartExist.isPresent()){
            Cart cart = cartExist.get();
            int sumQuantity = cart.getQuantity() + cartRequestDto.getQuantity();
            cart.setQuantity(sumQuantity);
        }
        // 없으면 새로 등록
        else{
            Cart cart = new Cart(userId, product, cartRequestDto.getQuantity());
            cartRepository.save(cart);
        }

        return getCartList(userId);
    }

    // cart 업데이트
    @Transactional
    public List<CartResponseDto> cartUpdate(Long userId, CartRequestDto cartRequestDto) {
        Product product = findByProduct(cartRequestDto.getProductId());
        
        Optional<Cart> cartExist = cartExist(userId, product.getId());
        // 장바구니에 동일한 상품이 존재하면 수량 더해서 업데이트
        if(cartExist.isPresent()){
            Cart cart = cartExist.get();
            cart.setQuantity(cartRequestDto.getQuantity());
        }

        return getCartList(userId);
    }
    
    // cart 삭제
    public List<CartResponseDto> cartDelete(Long userId, CartRequestDto cartRequestDto) {

        Optional<Cart> cartExist = cartExist(userId, cartRequestDto.getProductId());
        if(cartExist.isPresent()){
            Cart cart = cartExist.get();
            cartRepository.delete(cart);
        }

        return getCartList(userId);
    }

    private Product findByProduct(Long productId){
        return productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found with id:" + productId));
    }
    
    private Optional<Cart> cartExist(Long userId, Long productId){
        return cartRepository.findByUserIdAndProductId(userId, productId);
    }
}
