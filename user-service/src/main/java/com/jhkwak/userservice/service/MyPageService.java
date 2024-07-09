package com.jhkwak.userservice.service;

import com.jhkwak.userservice.dto.InfoUpdateRequestDto;
import com.jhkwak.userservice.dto.UserResponseDto;
import com.jhkwak.userservice.entity.User;
import com.jhkwak.userservice.jwt.JwtUtil;
import com.jhkwak.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Cacheable(cacheNames = "userInfo", key = "#userId")
    public UserResponseDto getUserInfo(Long userId) {
        return userRepository.findById(userId).map(UserResponseDto::new).orElseThrow(() -> new IllegalArgumentException("User not found with id:" + userId));
    }

    @Transactional
    public boolean updateUserInfo(Long Id, InfoUpdateRequestDto infoUpdateRequestDto) {

        User user = userRepository.findById(Id).get();

        if((infoUpdateRequestDto.getAddress() != null)){
            user.setAddress(infoUpdateRequestDto.getAddress());
        }

        if((infoUpdateRequestDto.getPhone() != null)){
            user.setPhone(infoUpdateRequestDto.getPhone());
        }

        if((infoUpdateRequestDto.getUpdatePassword() != null)){
            
            // 비밀번호가 일치하면 비밀번호 업데이트
            if(passwordEncoder.matches(infoUpdateRequestDto.getCurrentPassword(), user.getPassword())){
                String updatePassword = passwordEncoder.encode(infoUpdateRequestDto.getUpdatePassword());
                user.setPassword(updatePassword);
            }
            else{
                return false;
            }
        }
        
        return true;
    }
}
