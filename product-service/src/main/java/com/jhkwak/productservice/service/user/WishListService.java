package com.jhkwak.productservice.service.user;

import com.jhkwak.productservice.dto.product.WishRequestDto;
import com.jhkwak.productservice.dto.user.WishListResponseDto;
import com.jhkwak.productservice.entity.user.User;
import com.jhkwak.productservice.entity.user.WishList;
import com.jhkwak.productservice.repository.user.UserRepository;
import com.jhkwak.productservice.repository.user.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishListService {

    private final UserRepository userRepository;
    private final WishListRepository wishListRepository;

    public List<WishListResponseDto> getWishList(Long userId) {
        User user = userRepository.findById(userId).get();
        List<WishList> wishLists = user.getWishes();
        System.out.println(wishLists);

        return wishLists.stream()
                .map(wishList -> new WishListResponseDto(
                        wishList.getProduct().getId(),
                        wishList.getProduct().getName(),
                        wishList.getProduct().getPrice().longValue()
                ))
                .collect(Collectors.toList());

        // List<Map<String, Object>> results  = wishListRepository.findUserWishList(userId);
        // return results.stream()
        //             .map(result -> new WishListResponseDto(
        //                     (String) result.get("productName"),
        //                     Long.parseLong(String.valueOf(result.get("productPrice")))
        //             )).toList();
    }

    public List<WishListResponseDto> wishDelete(User user, WishRequestDto wishRequestDto) {
        WishList wish = wishListRepository.findByUserIdAndProductId(user.getId(), wishRequestDto.getProductId());

        wishListRepository.delete(wish);

        return getWishList(user.getId());
    }
}
