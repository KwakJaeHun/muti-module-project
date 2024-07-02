package com.jhkwak.userservice.controller;

import com.jhkwak.userservice.dto.user.LoginRequestDto;
import com.jhkwak.userservice.dto.user.SignupRequestDto;
import com.jhkwak.userservice.entity.Response;
import com.jhkwak.userservice.jwt.JwtUtil;
import com.jhkwak.userservice.service.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(LoginRequestDto loginRequestDto, HttpServletResponse res){

        return userService.login(loginRequestDto, res);

    }

    // 로그아웃
    @GetMapping("/logout")
    @ResponseBody
    public ResponseEntity<?> logout(HttpServletResponse response){

        // jwt 삭제
        jwtUtil.jwtDelete(response);

        return ResponseEntity.ok("Logout successful");
    }

    // 이메일 인증
    @GetMapping("/verify")
    @ResponseBody
    public ResponseEntity<?> emailVerification(@RequestParam String token){

        Response response = userService.emailVerification(token);

        return ResponseEntity.ok(response);

    }

    @PostMapping("/refresh-token")
    public void refreshToken(
        @RequestHeader("Authorization") String accessToken,
        @RequestHeader("RefreshToken") String refreshToken,
        HttpServletResponse res
    )
    {
        String token = jwtUtil.substringToken(accessToken);
        String userId = jwtUtil.getUserInfoFromToken(token).getSubject();

        if (!jwtUtil.validateToken(token) && jwtUtil.validateToken(refreshToken)) {
            String newAccessToken = jwtUtil.createAccessToken(Long.parseLong(userId));
            String newRefreshToken = jwtUtil.createRefreshToken();


            jwtUtil.addJwtToCookie(newAccessToken, res);
            res.addHeader("RefreshToken", newRefreshToken);
        } else {
            throw new IllegalArgumentException("Invalid refresh token or access token.");
        }
    }
}
