package com.jhkwak.userservice.config;

import com.jhkwak.userservice.fegin.FeignTokenHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                String token = FeignTokenHolder.getToken();
                if (token != null) {
                    requestTemplate.header("Authorization", token);
                }
                requestTemplate.header("Content-Type", "application/json");
            }
        };
    }
}
