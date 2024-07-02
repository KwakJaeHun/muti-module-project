package com.jhkwak.userservice.controller;

import com.jhkwak.userservice.dto.user.*;
import com.jhkwak.userservice.entity.Response;
import com.jhkwak.userservice.entity.ResponseCode;
import com.jhkwak.userservice.jwt.JwtUtil;
import com.jhkwak.userservice.service.user.CartService;
import com.jhkwak.userservice.service.user.MyPageService;
import com.jhkwak.userservice.service.user.WishListService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/my-page")
public class MyPageController {

    private final MyPageService myPageService;
    private final WishListService wishListService;
    private final CartService cartService;
    private final JwtUtil jwtUtil;

    // 메인
    @GetMapping("/main")
    public ResponseEntity<?> mainPage(
        @RequestHeader("X-Authenticated-User") String userId
    )
    {
        UserResponseDto dto = myPageService.getUserInfo(Long.parseLong(userId));
        return ResponseEntity.ok(dto);
    }

    // 주소 업데이트
    @PutMapping("/address")
    public ResponseEntity<?> updateAddress(
        @RequestHeader("X-Authenticated-User") String userId,
        @RequestBody InfoUpdateRequestDto infoUpdateRequestDto
    )
    {
        myPageService.updateUserInfo(Long.parseLong(userId), infoUpdateRequestDto);
        return ResponseEntity.ok("Success Change Address");
    }

    // 전화번호 업데이트
    @PutMapping("/phone")
    public ResponseEntity<?> updatePhone(
            @RequestHeader("X-Authenticated-User") String userId,
            @RequestBody InfoUpdateRequestDto infoUpdateRequestDto
    )
    {
        myPageService.updateUserInfo(Long.parseLong(userId), infoUpdateRequestDto);
        return ResponseEntity.ok("Success Change phone");
    }

    // 비밀번호 업데이트
    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(
            @RequestHeader("X-Authenticated-User") String userId,
            @RequestBody InfoUpdateRequestDto infoUpdateRequestDto
    )
    {
        if(myPageService.updateUserInfo(Long.parseLong(userId), infoUpdateRequestDto)){
            return ResponseEntity.ok("Success Change password");
        }
        else{
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Response(ResponseCode.USER_PASSWORD_WRONG));
        }
    }

    // wishList
    @GetMapping("/wish-list")
    public ResponseEntity<?> wishList(
            @RequestHeader("Authorization") String accessToken
    )
    {
        List<WishListResponseDto> wishlist = wishListService.getWishList(accessToken);
        return ResponseEntity.ok(wishlist);
    }

    // wish 삭제
    @DeleteMapping("/wish-delete")
    public ResponseEntity<?> wishDelete(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody WishRequestDto wishRequestDto
    )
    {
        List<WishListResponseDto> wishlist = wishListService.wishDelete(accessToken, wishRequestDto);
        return ResponseEntity.ok(wishlist);
    }

    // 장바구니
    @GetMapping("/cart-list")
    public ResponseEntity<?> cartList(
            @RequestHeader("Authorization") String accessToken
    )
    {
        List<CartResponseDto> cartList = cartService.getCartList(accessToken);
        return ResponseEntity.ok(cartList);

    }

    // 장바구니 업데이트
    @PutMapping("/cart-update")
    public ResponseEntity<?> cartUpdate(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody CartRequestDto cartRequestDto
    )
    {
        List<CartResponseDto> cartList = cartService.cartUpdate(accessToken, cartRequestDto);
        return ResponseEntity.ok(cartList);
    }

    // 장바구니 삭제
    @DeleteMapping("/cart-delete")
    public ResponseEntity<?> cartDelete(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody CartRequestDto cartRequestDto
    )
    {
        List<CartResponseDto> cartList = cartService.cartDelete(accessToken, cartRequestDto);
        return ResponseEntity.ok(cartList);
    }
}
