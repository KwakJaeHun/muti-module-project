package com.jhkwak.orderservice.feign;


import com.jhkwak.orderservice.config.FeignClientConfig;
import com.jhkwak.orderservice.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


//@FeignClient(name = "user-service", url = "http://api-gateway:9000/user", configuration = FeignClientConfig.class)
@FeignClient(name = "user-service", url = "http://localhost:9000/user", configuration = FeignClientConfig.class)
public interface UserClient {

    // userInfo 조회
    @GetMapping("/my-page/main")
    UserResponseDto userInfo();

}
