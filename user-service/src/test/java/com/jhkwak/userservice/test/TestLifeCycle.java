package com.jhkwak.userservice.test;

import org.junit.jupiter.api.*;

public class TestLifeCycle {

    @BeforeAll // 테스트 시작전 한번 호출 됨
    static void beforeAll(){
        System.out.println("## beforeAll Anootation 호출 ###");
        System.out.println();
    }

    @AfterAll // 테스트 마무리 후 한번 호출됨
    static void afterAll(){
        System.out.println("## afterAll Anootation 호출 ###");
        System.out.println();
    }

    @BeforeEach // 테스트 코드 메소드 실행 전 매번 호출 됨
    void beforeEach(){
        System.out.println("## beforeEach Anootation 호출 ###");
        System.out.println();
    }

    @AfterEach // 테스트 코드 메소드 실행 후 매번 호출 됨
    void afterEach(){
        System.out.println("## afterEach Anootation 호출 ###");
        System.out.println();
    }

    @Test
    void test1(){
        System.out.println("## test1 시작 ##");
        System.out.println();
    }

    @Test
    @DisplayName("Test Case 2")
    void test2(){
        System.out.println("## test2 시작 ##");
        System.out.println();
    }

    @Test
    @Disabled // 테스트를 실행하지 않게 설정하는 어노테이션
    void test3(){
        System.out.println("## test3 시작 ##");
        System.out.println();
    }

}
