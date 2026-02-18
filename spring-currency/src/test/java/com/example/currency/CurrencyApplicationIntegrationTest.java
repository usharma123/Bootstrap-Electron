package com.example.currency;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CurrencyApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = {
    "spring.main.banner-mode=off",
    "logging.level.root=WARN"
})
@DisplayName("CurrencyApplication Integration Tests")
class CurrencyApplicationIntegrationTest {

    @Test
    @DisplayName("Application should start without errors")
    void applicationShouldStartWithoutErrors() {
        // This integration test verifies the application context loads successfully
        assertNotNull(CurrencyApplication.class);
    }

    @Test
    @DisplayName("CurrencyApplication class should be instantiable")
    void currencyApplicationShouldBeInstantiable() {
        assertDoesNotThrow(() -> new CurrencyApplication());
    }
}