package com.jhkwak.userservice.service;

import com.jhkwak.userservice.dto.LoginRequestDto;
import com.jhkwak.userservice.dto.LoginResponseDto;
import com.jhkwak.userservice.dto.SignupRequestDto;
import com.jhkwak.userservice.entity.Response;
import com.jhkwak.userservice.entity.ResponseCode;
import com.jhkwak.userservice.entity.User;
import com.jhkwak.userservice.jwt.JwtUtil;
import com.jhkwak.userservice.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final MailService mailService;

    public Response signup(SignupRequestDto requestDto) {

        String name     = requestDto.getName();
        String email    = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String phone    = requestDto.getPhone();
        String address  = requestDto.getAddress();
        
        // 이메일 중복확인
        Optional<User> checkEmail = userRepository.findByEmail(email);
        
        if(checkEmail.isPresent()){
            return new Response(ResponseCode.USER_EMAIL_ALREADY_EXIST);
        }

        // 핸드폰 번호 중복확인
        Optional<User> checkPhone = userRepository.findByPhone(phone);

        if(checkPhone.isPresent()){
            return new Response(ResponseCode.USER_PHONE_ALREADY_EXIST);
        }

        // 이메일 인즌 토큰
        String token = UUID.randomUUID().toString();
        
        // 사용자 등록
        User user = new User(name, email, password, phone, address, token, false);
        userRepository.save(user);

        // 인증 이메일 발송
        sendVerificationEmail(user, token);

        return new Response(ResponseCode.USER_CREATE_SUCCESS);
    }
    
    // 인증 이메일 전송
    public void sendVerificationEmail(User user, String token){

        try {
            String url = "http://localhost:8080/user/verify?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8.name());

            String text = "<p>다음 링크를 클릭하여 이메일 인증을 완료하세요:</p><p><a href=\"" + url + "\">" + url + "</a></p>";

            mailService.sendEmail(user.getEmail(), "회원가입 이메일 인증", text);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    
    // 이메일 인증
    @Transactional
    public Response emailVerification(String token) {
        Optional<User> checkToken = userRepository.findByEmailVerificationTokenAndEmailVerifiedStatus(token, false);

        if(checkToken.isPresent()){
            // 만료시간이 지났으면 인증 메일 재전송 + 확인 필요 응답
            if(!checkExpirationTime(checkToken.get(), "update")){
                return new Response(ResponseCode.RE_VERIFICATION_EMAIL);
            }
        }
        else{
            // 인증 완료된 메일입니다 or 토큰이 올바르지 않습니다 두가지 분기처리 필요!
            return new Response(ResponseCode.TOKEN_DO_NOT_MATCH);
        }

        return new Response(ResponseCode.EMAIL_VERIFICATION_SUCCESS);
    }

    @Transactional
    public ResponseEntity<?> login(LoginRequestDto loginRequestDto, HttpServletResponse res) {

        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        try {
            // 사용자 확인
            User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Not registered - Please try again"));;

            // 이메일 인증을 진행하지 않은 경우
            if(!user.getEmailVerifiedStatus()){
                // 만료 시간이 지나지 않았으면 인증 확인 응답
                if(checkExpirationTime(user, "unUpdate")){
                    Response response = new Response(ResponseCode.REQUIRE_VERIFICATION_EMAIL);
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
                }
                else{
                    Response response = new Response(ResponseCode.RE_VERIFICATION_EMAIL);
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
                }
            }

            // 비밀번호 확인
            if(!passwordEncoder.matches(password, user.getPassword())){
                Response response = new Response(ResponseCode.USER_PASSWORD_WRONG);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            // Access Token 및 Refresh Token 생성
            String accessToken = jwtUtil.createAccessToken(user.getId());
            String refreshToken = jwtUtil.createRefreshToken();

            // Refresh Token을 응답 헤더에 추가
            res.addHeader("RefreshToken", refreshToken);
            res.addHeader("AccessToken", accessToken);

            // 로그인 성공 응답에 발급받은 토큰들 추가
            LoginResponseDto responseDTO = new LoginResponseDto(accessToken, refreshToken, "Login successful!");
            return ResponseEntity.ok(responseDTO);
        }
        catch (IllegalArgumentException e){
            LoginResponseDto responseDTO = new LoginResponseDto(null, null, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDTO);
        }
    }

    // 인증 만료시간 체크
    public boolean checkExpirationTime(User user, String updateStatus){
        
        // 만료 시간이 지났으면 인증 메일 재전송
        if(LocalDateTime.now().minusHours(24).isAfter(user.getEmailVerificationExpiresAt())){
            String token = creatToken();
            // 만료된 토큰 업데이트
            user.setEmailVerificationToken(token);
            // 만료 시간 갱신
            user.setEmailVerificationExpiresAt(LocalDateTime.now().plusHours(24));
            
            // 인증 메일 재전송
            sendVerificationEmail(user, token);
            return false;
        }
        else{
            if(updateStatus.equals("update")){
                // 인증된 토큰 상태값 업데이트
                user.setEmailVerifiedStatus(true);
            }
        }
        
        return true;
    }
    
    // 이메일 인증 토큰 생성
    public String creatToken(){
        return UUID.randomUUID().toString();
    }
}
