package com.jhkwak.userservice.service;

import com.jhkwak.userservice.dto.CartRequestDto;
import com.jhkwak.userservice.dto.CartResponseDto;
import com.jhkwak.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final UserRepository userRepository;

    public List<CartResponseDto> getCartList(String accessToken) {
        
        // 장바구니 호출하여 리스트 받아오기
        

        return List.of();
    }
    
    // 장바구니 업데이트
    @Transactional
    public List<CartResponseDto> cartUpdate(String accessToken, CartRequestDto cartRequestDto) {
        
        // 장바구니 호출하여 업데이트 하기

        return List.of();
    }
    
    // 장바구니 삭제
    // @Transactional
    public List<CartResponseDto> cartDelete(String accessToken, CartRequestDto cartRequestDto) {
        
        // 장바구니 삭제 후 현재 장바구니 리스트 가져오기

        return List.of();
    }
}
