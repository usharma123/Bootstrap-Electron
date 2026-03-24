package com.example.currency;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DisplayName("CurrencyApplication Tests")
class CurrencyApplicationTest {

    @Test
    @DisplayName("Application context should load successfully")
    void contextLoads() {
        // This test verifies the Spring application context loads without errors
    }

    @Test
    @DisplayName("Main method should not throw exceptions")
    void mainMethodShouldNotThrowExceptions() {
        assertDoesNotThrow(() -> CurrencyApplication.main(new String[]{"--spring.main.web-application-type=none", "--server.port=0"}));
    }
}
