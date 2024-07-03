package com.jhkwak.userservice.service;

import com.jhkwak.userservice.dto.WishListResponseDto;
import com.jhkwak.userservice.dto.WishRequestDto;
import com.jhkwak.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishListService {

    private final UserRepository userRepository;
    private final WebClient.Builder webClientBuilder;

    public List<WishListResponseDto> getWishList(String accessToken) {
        
        // accessToken을 활용한 product 서비스 호출


        return List.of();
    }
    
    public List<WishListResponseDto> wishDelete(String accessToken, WishRequestDto wishRequestDto) {
        // accessToken을 활용한 product 서비스 호출

        return List.of();
    }
}
