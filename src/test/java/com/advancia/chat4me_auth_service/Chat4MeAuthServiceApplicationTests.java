package com.advancia.chat4me_auth_service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Chat4MeAuthServiceApplicationTests {
    @Test
    void contextLoads() {
        assertDoesNotThrow(() -> Chat4MeAuthServiceApplication.main(new String[]{}));
    }
}