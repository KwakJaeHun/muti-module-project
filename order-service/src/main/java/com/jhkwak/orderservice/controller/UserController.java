package com.jhkwak.orderservice.controller;

import com.jhkwak.orderservice.dto.user.LoginRequestDto;
import com.jhkwak.orderservice.dto.user.SignupRequestDto;
import com.jhkwak.orderservice.entity.Response;
import com.jhkwak.orderservice.jwt.JwtUtil;
import com.jhkwak.orderservice.security.UserDetailImpl;
import com.jhkwak.orderservice.service.user.UserService;
import com.jhkwak.orderservice.service.user.WishListService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    // 회원가입
    @GetMapping("/signup-page")
    public String signup(){

        return "user/signup";
    }


    // 회원가입
    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<?> signup(SignupRequestDto requestDto){

        Response response = userService.signup(requestDto);

        return ResponseEntity.ok(response);
    }

    // 로그인 페이지
    @GetMapping("/login-page")
    public String loginPage(){

        return "user/login";

    }
    
    // 로그인
    @GetMapping("/login-email-verification")
    @ResponseBody
    public ResponseEntity<?> login(@AuthenticationPrincipal UserDetailImpl userDetails){

        Response response = userService.login(userDetails.getUser());

        return ResponseEntity.ok(response);
    }

    // 로그아웃
    @GetMapping("/logout")
    @ResponseBody
    public ResponseEntity<?> logout(HttpServletResponse response){

        // jwt 삭제
        jwtUtil.jwtDelete(response);

        return ResponseEntity.ok().build();
    }

    
    
    // 이메일 인증
    @GetMapping("/verify")
    @ResponseBody
    public ResponseEntity<?> emailVerification(@RequestParam String token){

        Response response = userService.emailVerification(token);

        return ResponseEntity.ok(response);

    }
}
