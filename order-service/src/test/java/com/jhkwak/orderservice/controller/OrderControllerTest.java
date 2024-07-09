package com.jhkwak.orderservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhkwak.orderservice.dto.OrderListRequestDto;
import com.jhkwak.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderService orderService;
    
    @Test
    public void 내_PC_코어수_알아보기(){
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println(availableProcessors);
    }

    @Test
    public void 동시에_10000명이_주문을한다() throws Exception {

        int numberOfThreads = 10000;
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()); // PC 코어수 만큼 ThreadPool 생성
        List<Callable<Void>> tasks = new ArrayList<>(); // 실행할 작업 리스트를 생성합니다.

        // given
        String accessToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMyIsImV4cCI6MTcyMDQ5MDY2NiwiaWF0IjoxNzIwNDA0MjY2fQ.hqruT1HdxPiLi6p3oIeqYkaqTL56p3LE2wpyzdTav6w";
        String userId = "2";
        OrderListRequestDto order1 = new OrderListRequestDto();
        order1.setProductId(1L);
        order1.setQuantity(1);
        order1.setPrice(10000L);

        List<OrderListRequestDto> orderList = Arrays.asList(order1);
        String orderListJson = objectMapper.writeValueAsString(orderList);

        // when & then
        // 각 요청에 대한 작업 생성
        for (int i = 0; i < numberOfThreads; i++) { // 100개의 작업을 생성하여 리스트에 추가합니다.
            tasks.add(() -> {
                mockMvc.perform(post("/order/check-out")
                                .header("Authorization", accessToken)
                                .header("X-Authenticated-User", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderListJson))
                        //.andExpect(status().isOk()) // 200 아닐시 오류 출력
                        .andDo(print()); // 요청과 응답을 출력
                return null;
            });
        }

        // 모든 작업 비동기 실행
        List<Future<Void>> futures = executorService.invokeAll(tasks); // 모든 작업을 비동기적으로 실행

        // 모든 작업 완료 대기
        for (Future<Void> future : futures) {
            future.get();  // 각 작업이 완료될 때까지 대기합니다. 예외 발생 시 테스트 실패 처리 가능
        }

        // ExecutorService 종료
        executorService.shutdown(); // 모든 작업이 완료된 후 스레드 풀을 종료합니다.
    }
}
