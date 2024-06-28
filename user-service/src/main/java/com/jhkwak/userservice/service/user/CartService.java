package com.jhkwak.userservice.service.user;

import com.jhkwak.userservice.dto.user.CartRequestDto;
import com.jhkwak.userservice.dto.user.CartResponseDto;
import com.jhkwak.userservice.entity.product.Product;
import com.jhkwak.userservice.entity.user.Cart;
import com.jhkwak.userservice.entity.user.User;
import com.jhkwak.userservice.repository.product.ProductRepository;
import com.jhkwak.userservice.repository.user.CartRepository;
import com.jhkwak.userservice.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public List<CartResponseDto> getCartList(Long userId) {
        User user = findByuser(userId);
        List<Cart> cartLists = user.getCarts();

        return cartLists.stream()
                .map(cartList -> new CartResponseDto(
                        cartList.getProduct().getId(),
                        cartList.getProduct().getName(),
                        cartList.getQuantity(),
                        cartList.getProduct().getPrice() * cartList.getQuantity(),
                        cartList.getProduct().getProductStock().getStockQuantity())
                ).toList();

    }

    // 장바구니 등록
    @Transactional
    public List<CartResponseDto> cartRegistration(Long userId, CartRequestDto cartRequestDto) {

        User user = findByuser(userId);
        Product product = findByProduct(cartRequestDto.getProductId());

        Optional<Cart> cartExist = cartRepository.findByUserIdAndProductId(userId, product.getId());
        // 장바구니에 동일한 상품이 존재하면 수량 더해서 업데이트
        if(cartExist.isPresent()){
            Cart cart = cartExist.get();
            int sumQuantity = cart.getQuantity() + cartRequestDto.getQuantity();
            cart.setQuantity(sumQuantity);
        }
        // 없으면 새로 등록
        else{
            Cart cart = new Cart(user, product, cartRequestDto.getQuantity());
            cartRepository.save(cart);
        }

        return getCartList(userId);
    }
    
    // 장바구니 업데이트
    @Transactional
    public List<CartResponseDto> cartUpdate(Long userId, CartRequestDto cartRequestDto) {

        Product product = findByProduct(cartRequestDto.getProductId());

        Optional<Cart> cartExist = cartRepository.findByUserIdAndProductId(userId, product.getId());
        // 장바구니에 동일한 상품이 존재하면 수량 업데이트
        if(cartExist.isPresent()){
            Cart cart = cartExist.get();
            cart.setQuantity(cartRequestDto.getQuantity());
        }

        return getCartList(userId);
    }
    
    // 장바구니 삭제
    // @Transactional
    public List<CartResponseDto> cartDelete(Long userId, CartRequestDto cartRequestDto) {

         Optional<Cart> cartExist = cartRepository.findByUserIdAndProductId(userId, cartRequestDto.getProductId());
         // 장바구니에 상품이 있으면 상품 삭제
         if(cartExist.isPresent()){
             cartRepository.delete(cartExist.get());
         }
        
        // @Transactional 어노테이션을 달아도 삭제가 안됨
        // JPA 메서드를 사용시 Transactional 을 달아줘야하는데 
        // getCartList에서 User의 CartList 를 조회하며 cart 테이블이 최신으로 유지 되면서 삭제가 안된다고 생각됨
        // cartRepository.deleteByUserIdAndProductId(userId, cartRequestDto.getProductId());

        return getCartList(userId);
    }

    private User findByuser(Long userId) {
        return userRepository.findById(userId).get();
    }

    private Product findByProduct(Long productId){
        return productRepository.findById(productId).get();
    }
}
