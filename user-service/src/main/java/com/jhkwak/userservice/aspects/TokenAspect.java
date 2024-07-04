package com.jhkwak.userservice.aspects;

import com.jhkwak.userservice.fegin.FeignTokenHolder;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TokenAspect {

    // Feign 호출시 accessToken setting
    @Before("(execution(* com.jhkwak.userservice.controller.MyPageController.wishList(..)) || " +
            "execution(* com.jhkwak.userservice.controller.MyPageController.wishDelete(..)) || " +
            "execution(* com.jhkwak.userservice.controller.MyPageController.cartList(..)) || " +
            "execution(* com.jhkwak.userservice.controller.MyPageController.cartUpdate(..)) || " +
            "execution(* com.jhkwak.userservice.controller.MyPageController.cartDelete(..)) || " +
            "execution(* com.jhkwak.userservice.controller.MyPageController.orderList(..)))" +
            "&& args(accessToken,..)")
    public void setFeignToken(String accessToken) {
        FeignTokenHolder.setToken(accessToken);
    }
}
