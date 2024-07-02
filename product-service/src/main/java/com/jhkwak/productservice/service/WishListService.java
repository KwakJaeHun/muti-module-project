package com.jhkwak.productservice.service;

import com.jhkwak.productservice.dto.WishRequestDto;
import com.jhkwak.productservice.dto.WishResponseDto;
import com.jhkwak.productservice.entity.Product;
import com.jhkwak.productservice.entity.WishList;
import com.jhkwak.productservice.repository.ProductRepository;
import com.jhkwak.productservice.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishListService {

    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;

    // wish-list 조회
    public List<WishResponseDto> getWishList(Long userId) {

        return wishListRepository.findByUserId(userId).stream()
                .map(wishList -> new WishResponseDto(
                        wishList.getProduct().getId(),
                        wishList.getProduct().getName(),
                        wishList.getProduct().getPrice()
                ))
                .toList();
    }

    // wish-list 등록
    public void wishAdd(Long userId, WishRequestDto wishRequestDto) {
        Product product = productRepository.findById(wishRequestDto.getProductId()).orElseThrow(() -> new IllegalArgumentException("Product not found with id:" + wishRequestDto.getProductId()));

        WishList wishList = new WishList(userId, product);
        wishListRepository.save(wishList);
    }
    
    // wish-list 삭제
    public List<WishResponseDto> wishDelete(Long userId, WishRequestDto wishRequestDto) {
        Optional<WishList> wish = wishListRepository.findByUserIdAndProductId(userId, wishRequestDto.getProductId());

        if(wish.isPresent()){
            wishListRepository.delete(wish.get());
        }

        return getWishList(userId);
    }
}
