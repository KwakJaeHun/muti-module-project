package com.jhkwak.userservice.controller;

import com.jhkwak.userservice.dto.product.WishRequestDto;
import com.jhkwak.userservice.dto.user.*;
import com.jhkwak.userservice.entity.Response;
import com.jhkwak.userservice.entity.ResponseCode;
import com.jhkwak.userservice.security.UserDetailImpl;
import com.jhkwak.userservice.service.user.CartService;
import com.jhkwak.userservice.service.user.MyPageService;
import com.jhkwak.userservice.service.user.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/my-page")
public class MyPageController {

    private final MyPageService myPageService;
    private final WishListService wishListService;
    private final CartService cartService;

    // 메인
    @GetMapping("/main")
    public ResponseEntity<?> mainPage(@AuthenticationPrincipal UserDetailImpl userDetail)
    {
        List<UserResponseDto> userInfo  = myPageService.getUserInfo(userDetail.getId());
        return ResponseEntity.ok(userInfo);
    }

    // 주소 업데이트
    @PutMapping("/address")
    public ResponseEntity<?> updateAddress(
        @AuthenticationPrincipal UserDetailImpl userDetail,
        @RequestBody InfoUpdateRequestDto infoUpdateRequestDto
    )
    {
        System.out.println("저속확인");
        myPageService.updateUserInfo(userDetail.getId(), infoUpdateRequestDto);
        return ResponseEntity.ok().build();
    }

    // 전화번호 업데이트
    @PutMapping("/phone")
    public ResponseEntity<?> updatePhone(
        @AuthenticationPrincipal UserDetailImpl userDetail,
        @RequestBody InfoUpdateRequestDto infoUpdateRequestDto
    )
    {
        myPageService.updateUserInfo(userDetail.getId(), infoUpdateRequestDto);
        return ResponseEntity.ok().build();
    }

    // 비밀번호 업데이트
    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(
        @AuthenticationPrincipal UserDetailImpl userDetail,
        @RequestBody InfoUpdateRequestDto infoUpdateRequestDto
    )
    {
        if(myPageService.updateUserInfo(userDetail.getId(), infoUpdateRequestDto)){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Response(ResponseCode.USER_PASSWORD_WRONG));
        }
    }

    // wishList
    @GetMapping("/wish-list")
    public ResponseEntity<?> wishList(@AuthenticationPrincipal UserDetailImpl userDetail)
    {
        List<WishListResponseDto> wishlist = wishListService.getWishList(userDetail.getId());
        return ResponseEntity.ok(wishlist);
    }

    // wish 삭제
    @DeleteMapping("/wish-delete")
    public ResponseEntity<?> wishDelete(
            @AuthenticationPrincipal UserDetailImpl userDetail,
            @RequestBody WishRequestDto wishRequestDto
    )
    {
        List<WishListResponseDto> wishlist = wishListService.wishDelete(userDetail.getUser(), wishRequestDto);
        return ResponseEntity.ok(wishlist);
    }

    // 장바구니
    @GetMapping("/cart-list")
    public ResponseEntity<?> cartList(@AuthenticationPrincipal UserDetailImpl userDetail)
    {
        List<CartResponseDto> cartList = cartService.getCartList(userDetail.getId());
        return ResponseEntity.ok(cartList);

    }

    // 장바구니 업데이트
    @PutMapping("/cart-update")
    public ResponseEntity<?> cartUpdate(
            @AuthenticationPrincipal UserDetailImpl userDetail,
            @RequestBody CartRequestDto cartRequestDto
    )
    {
        List<CartResponseDto> cartList = cartService.cartUpdate(userDetail.getId(), cartRequestDto);
        return ResponseEntity.ok(cartList);
    }


    // 장바구니 삭제
    @DeleteMapping("/cart-delete")
    public ResponseEntity<?> cartDelete(
            @AuthenticationPrincipal UserDetailImpl userDetail,
            @RequestBody CartRequestDto cartRequestDto
    )
    {
        List<CartResponseDto> cartList = cartService.cartDelete(userDetail.getId(), cartRequestDto);
        return ResponseEntity.ok(cartList);
    }
}
