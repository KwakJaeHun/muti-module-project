package com.jhkwak.orderservice.aspects;

import com.jhkwak.orderservice.feign.FeignTokenHolder;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TokenAspect {

    // Feign 호출시 accessToken setting
    @Before("execution(* com.jhkwak.orderservice.service.*.*(..)) && args(accessToken,..)")
    public void setFeignToken(String accessToken) {
        FeignTokenHolder.setToken(accessToken);
    }
}
