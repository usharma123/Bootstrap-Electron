package com.example.currency;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

/**
 * Test that uses Mockito mocking to directly test the main method
 * and achieve full coverage of CurrencyApplication.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CurrencyApplication Main Method Coverage Tests")
class CurrencyApplicationMainMethodTest {

    @Test
    @DisplayName("main method should call SpringApplication.run")
    void mainMethodShouldCallSpringApplicationRun() {
        try (MockedStatic<SpringApplication> mockedSpringApplication = Mockito.mockStatic(SpringApplication.class)) {
            mockedSpringApplication.when(() -> SpringApplication.run(any(Class.class), any(String[].class)))
                    .thenReturn(null);

            // Call the main method
            CurrencyApplication.main(new String[]{});

            // Verify SpringApplication.run was called with correct arguments
            mockedSpringApplication.verify(() ->
                SpringApplication.run(eq(CurrencyApplication.class), any(String[].class))
            );
        }
    }

    @Test
    @DisplayName("main method should handle empty arguments")
    void mainMethodShouldHandleEmptyArguments() {
        try (MockedStatic<SpringApplication> mockedSpringApplication = Mockito.mockStatic(SpringApplication.class)) {
            mockedSpringApplication.when(() -> SpringApplication.run(any(Class.class), any(String[].class)))
                    .thenReturn(null);

            assertDoesNotThrow(() -> CurrencyApplication.main(new String[]{}));
        }
    }

    @Test
    @DisplayName("main method should handle null arguments")
    void mainMethodShouldHandleNullArguments() {
        try (MockedStatic<SpringApplication> mockedSpringApplication = Mockito.mockStatic(SpringApplication.class)) {
            mockedSpringApplication.when(() -> SpringApplication.run(any(Class.class), any(String[].class)))
                    .thenReturn(null);

            assertDoesNotThrow(() -> CurrencyApplication.main(null));
        }
    }
}