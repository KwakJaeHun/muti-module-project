package com.jhkwak.apigateway.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URLDecoder;
import java.util.ArrayList;

@Component
public class JwtFilter implements WebFilter, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private final JwtTokenProvider jwtTokenProvider;
    private ApplicationContext applicationContext;

    private static final String[] EXCLUDED_PATHS = {
            "/",
            "/user/signup-page",
            "/user/signup",
            "/user/login-page",
            "/user/login",
            "/user/verify",
            "/product/list",
            "/product/detail/**"
    };

    public JwtFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String requestPath = exchange.getRequest().getURI().getPath();
        logger.info("Request path: {}", requestPath);
        AntPathMatcher pathMatcher = new AntPathMatcher();

        for (String excludedPath : EXCLUDED_PATHS) {
            if (pathMatcher.match(excludedPath, requestPath)) {
                logger.info("Path {} is excluded from JWT filtering", requestPath);
                return chain.filter(exchange);
            }
        }

        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        // token parse
        if(StringUtils.hasText(token)){
            token = URLDecoder.decode(token);
            token = jwtTokenProvider.substringToken(token);
        }

        if (token != null && jwtTokenProvider.validateToken(token)) {
            String userId = jwtTokenProvider.getFromToken(token);
            logger.info("Extracted userId from token: {}", userId);

            if (userId != null) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        new User(userId, "", new ArrayList<>()), null, new ArrayList<>()
                );
                return chain.filter(exchange.mutate()
                                .request(exchange.getRequest().mutate()
                                        .header("X-Authenticated-User", userId)
                                        .build())
                                .build())
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
            } else {
                logger.error("Failed to extract userId from token");
            }
        } else {
            logger.error("Invalid or expired token");
        }

        return chain.filter(exchange);
    }
}
