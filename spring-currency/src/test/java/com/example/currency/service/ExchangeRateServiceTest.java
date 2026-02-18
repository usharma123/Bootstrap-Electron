package com.example.currency.service;

import com.example.currency.model.ConversionRequest;
import com.example.currency.model.ConversionResponse;
import com.example.currency.model.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ExchangeRateService Tests")
class ExchangeRateServiceTest {

    private ExchangeRateService service;

    @BeforeEach
    void setUp() {
        service = new ExchangeRateService();
    }

    @Nested
    @DisplayName("Currency Conversion Tests")
    class CurrencyConversionTests {

        @Test
        @DisplayName("Should convert USD to EUR correctly")
        void convertUsdToEur() {
            ConversionRequest request = new ConversionRequest(100.0, Currency.USD, Currency.EUR);
            ConversionResponse response = service.convert(request);

            assertEquals(100.0, response.amount());
            assertEquals("USD", response.from());
            assertEquals("EUR", response.to());
            assertEquals(92.0, response.result());
            assertEquals(0.92, response.rate());
        }

        @Test
        @DisplayName("Should convert EUR to USD correctly")
        void convertEurToUsd() {
            ConversionRequest request = new ConversionRequest(100.0, Currency.EUR, Currency.USD);
            ConversionResponse response = service.convert(request);

            assertEquals(108.7, response.result(), 0.1);
            assertEquals(1.087, response.rate(), 0.001);
        }

        @Test
        @DisplayName("Should convert USD to JPY correctly")
        void convertUsdToJpy() {
            ConversionRequest request = new ConversionRequest(100.0, Currency.USD, Currency.JPY);
            ConversionResponse response = service.convert(request);

            assertEquals(14950.0, response.result());
            assertEquals(149.5, response.rate());
        }

        @Test
        @DisplayName("Should convert same currency with rate 1.0")
        void convertSameCurrency() {
            ConversionRequest request = new ConversionRequest(250.0, Currency.GBP, Currency.GBP);
            ConversionResponse response = service.convert(request);

            assertEquals(250.0, response.result());
            assertEquals(1.0, response.rate());
        }

        @Test
        @DisplayName("Should convert GBP to EUR correctly")
        void convertGbpToEur() {
            ConversionRequest request = new ConversionRequest(50.0, Currency.GBP, Currency.EUR);
            ConversionResponse response = service.convert(request);

            assertEquals(58.23, response.result(), 0.01);
            assertTrue(response.rate() > 0);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle zero amount")
        void convertZeroAmount() {
            ConversionRequest request = new ConversionRequest(0.0, Currency.USD, Currency.EUR);
            ConversionResponse response = service.convert(request);

            assertEquals(0.0, response.amount());
            assertEquals(0.0, response.result());
        }

        @Test
        @DisplayName("Should handle large amount")
        void convertLargeAmount() {
            ConversionRequest request = new ConversionRequest(1000000.0, Currency.USD, Currency.CHF);
            ConversionResponse response = service.convert(request);

            assertEquals(1000000.0, response.amount());
            assertEquals(880000.0, response.result());
        }

        @Test
        @DisplayName("Should handle small fractional amount")
        void convertSmallAmount() {
            ConversionRequest request = new ConversionRequest(0.01, Currency.EUR, Currency.GBP);
            ConversionResponse response = service.convert(request);

            assertEquals(0.01, response.amount());
            assertTrue(response.rate() > 0);
        }
    }

    @Nested
    @DisplayName("All Currency Pairs")
    class AllCurrencyPairsTests {

        @Test
        @DisplayName("Should support all currency conversions")
        void allCurrencyConversions() {
            Currency[] currencies = Currency.values();

            for (Currency from : currencies) {
                for (Currency to : currencies) {
                    ConversionRequest request = new ConversionRequest(100.0, from, to);
                    ConversionResponse response = service.convert(request);

                    assertNotNull(response);
                    assertEquals("100.0", String.valueOf(response.amount()));
                    assertEquals(from.name(), response.from());
                    assertEquals(to.name(), response.to());
                    assertTrue(response.rate() > 0);
                }
            }
        }

        @Test
        @DisplayName("Should have correct number of currencies")
        void correctCurrencyCount() {
            assertEquals(10, Currency.values().length);
        }
    }
}
