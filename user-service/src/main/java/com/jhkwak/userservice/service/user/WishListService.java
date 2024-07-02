package com.jhkwak.userservice.service.user;

import com.jhkwak.userservice.dto.user.WishListResponseDto;
import com.jhkwak.userservice.dto.user.WishRequestDto;
import com.jhkwak.userservice.entity.user.User;
import com.jhkwak.userservice.repository.user.UserRepository;
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
