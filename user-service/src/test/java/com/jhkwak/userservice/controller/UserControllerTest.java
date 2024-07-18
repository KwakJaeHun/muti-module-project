package com.jhkwak.userservice.controller;

import com.jhkwak.userservice.dto.LoginRequestDto;
import com.jhkwak.userservice.dto.SignupRequestDto;
import com.jhkwak.userservice.dto.UserResponseDto;
import com.jhkwak.userservice.entity.Response;
import com.jhkwak.userservice.entity.ResponseCode;
import com.jhkwak.userservice.jwt.JwtUtil;
import com.jhkwak.userservice.service.TokenService;
import com.jhkwak.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    private MockMvc mockMvc;

    // UserService를 Mock으로 생성
    @Mock
    private UserService userService;

    // JwtUtil을 Mock으로 생성
    @Mock
    private JwtUtil jwtUtil;

    // TokenService를 Mock으로 생성
    @Mock
    private TokenService tokenService;

    @Captor
    private ArgumentCaptor<SignupRequestDto> signupRequestDtoCaptor;

    // UserController에 위에서 생성한 Mock들을 주입
    @InjectMocks
    private UserController userController;

    // 테스트 전에 초기 설정을 진행
    @BeforeEach
    public void setup() {
        // MockitoAnnotations를 이용해 Mock 객체들을 초기화
        MockitoAnnotations.openMocks(this);
        // MockMvc를 standaloneSetup으로 설정
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    // 회원가입 페이지를 테스트하는 메서드
    @Test
    public void testSignupPage() throws Exception {
        // GET 요청을 /user/signup-page로 보내고
        mockMvc.perform(get("/user/signup-page"))
                // HTTP 상태가 200인지 확인
                .andExpect(status().isOk())
                // 반환되는 뷰 이름이 "user/signup"인지 확인
                .andExpect(view().name("user/signup"));
    }

    // 회원가입 기능을 테스트하는 메서드
    @Test
    public void testSignup() throws Exception {
        // userService.signup 메서드가 호출되면 "User registered successfully" 메시지를 가진 Response 객체를 반환하도록 설정
        when(userService.signup(any(SignupRequestDto.class))).thenReturn(new Response(ResponseCode.USER_CREATE_SUCCESS));

        // POST 요청을 /user/signup으로 보내고, 필요한 파라미터들을 설정
        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "testUser")
                        .param("email", "test@example.com")
                        .param("password", "password123")
                        .param("phone", "010-0000-0000")
                        .param("address", "testAddress")
                )
                // HTTP 상태가 200인지 확인
                .andExpect(status().isOk())
                // 반환되는 JSON의 "message" 필드가 "User registered successfully"인지 확인
                .andExpect(jsonPath("$.success").exists())
                .andExpect(jsonPath("$.stateCode").exists())
                .andExpect(jsonPath("$.description").exists())
                .andDo(print());

        // ArgumentCaptor를 사용하여 전달된 SignupRequestDto 객체를 캡처
        verify(userService).signup(signupRequestDtoCaptor.capture());
        SignupRequestDto capturedArgument = signupRequestDtoCaptor.getValue();

        // 전달된 인스턴스의 필드 값들을 검증
        assertEquals("testUser", capturedArgument.getName());
        assertEquals("password123", capturedArgument.getPassword());
        assertEquals("test@example.com", capturedArgument.getEmail());
        assertEquals("010-0000-0000", capturedArgument.getPhone());
        assertEquals("testAddress", capturedArgument.getAddress());
    }

    // 로그인 페이지를 테스트하는 메서드
    @Test
    public void testLoginPage() throws Exception {
        // GET 요청을 /user/login-page로 보내고
        mockMvc.perform(get("/user/login-page"))
                // HTTP 상태가 200인지 확인
                .andExpect(status().isOk())
                // 반환되는 뷰 이름이 "user/login"인지 확인
                .andExpect(view().name("user/login"));
    }

    // 로그인 기능을 테스트하는 메서드
    @Test
    public void testLogin() throws Exception {
        // UserService의 login 메서드가 호출되면 "Login successful" 메시지를 가진 UserResponseDto 객체를 반환하도록 설정
        UserResponseDto userResponseDto = new UserResponseDto(200, "Login successful");
        when(userService.login(any(LoginRequestDto.class), any())).thenReturn(userResponseDto);

        // POST 요청을 /user/login으로 보내고, 필요한 파라미터들을 설정
        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "testUser")
                        .param("password", "password123"))
                // HTTP 상태가 200인지 확인
                .andExpect(status().isOk())
                // 반환되는 JSON의 "message" 필드가 "Login successful"인지 확인
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    // 로그아웃 기능을 테스트하는 메서드
    @Test
    public void testLogout() throws Exception {
        // jwtUtil의 jwtDelete 메서드와 tokenService의 addToBlacklist 메서드가 호출될 때 아무런 동작도 하지 않도록 설정
        doNothing().when(jwtUtil).jwtDelete(any());
        doNothing().when(tokenService).addToBlacklist(anyString());

        // GET 요청을 /user/logout으로 보내고, 필요한 헤더들을 설정
        mockMvc.perform(get("/user/logout")
                        .header("Authorization", "Bearer accessToken")
                        .header("RefreshToken", "refreshToken"))
                // HTTP 상태가 200인지 확인
                .andExpect(status().isOk())
                // 반환되는 내용이 "Logout successful"인지 확인
                .andExpect(content().string("Logout successful"));
    }

    // 이메일 인증 기능을 테스트하는 메서드
//    @Test
//    public void testEmailVerification() throws Exception {
//        // userService의 emailVerification 메서드가 호출되면 "Email verified successfully" 메시지를 가진 Response 객체를 반환하도록 설정
//        when(userService.emailVerification(anyString())).thenReturn(new Response("success", "Email verified successfully"));
//
//        // GET 요청을 /user/verify로 보내고, 필요한 파라미터를 설정
//        mockMvc.perform(get("/user/verify")
//                        .param("token", "verificationToken"))
//                // HTTP 상태가 200인지 확인
//                .andExpect(status().isOk())
//                // 반환되는 JSON의 "message" 필드가 "Email verified successfully"인지 확인
//                .andExpect(jsonPath("$.message").value("Email verified successfully"));
//    }

    // 토큰 갱신 기능을 테스트하는 메서드
//    @Test
//    public void testRefreshToken() throws Exception {
//        // jwtUtil과 tokenService 메서드들이 호출될 때의 동작을 설정
//        when(jwtUtil.substringToken(anyString())).thenReturn("token");
//        when(jwtUtil.getUserInfoFromToken(anyString())).thenReturn(new org.springframework.security.core.userdetails.User("1", "", new ArrayList<>()));
//        when(jwtUtil.validateToken(anyString())).thenReturn(false);
//        when(jwtUtil.validateToken("refreshToken")).thenReturn(true);
//        when(jwtUtil.createAccessToken(anyLong())).thenReturn("newAccessToken");
//        when(jwtUtil.createRefreshToken()).thenReturn("newRefreshToken");
//        doNothing().when(tokenService).addToBlacklist(anyString());
//        doNothing().when(jwtUtil).addJwtToCookie(anyString(), any());
//
//        // POST 요청을 /user/refresh-token으로 보내고, 필요한 헤더들을 설정
//        mockMvc.perform(post("/user/refresh-token")
//                        .header("Authorization", "Bearer accessToken")
//                        .header("RefreshToken", "refreshToken"))
//                // HTTP 상태가 200인지 확인
//                .andExpect(status().isOk());
//    }
}
