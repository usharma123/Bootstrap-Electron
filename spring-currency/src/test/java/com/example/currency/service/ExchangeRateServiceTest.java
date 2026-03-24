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
    @DisplayName("Basic Conversion Tests")
    class BasicConversionTests {

        @Test
        @DisplayName("convert should return correct response for USD to EUR")
        void convertShouldReturnCorrectResponseForUsdToEur() {
            ConversionRequest request = new ConversionRequest(100.0, Currency.USD, Currency.EUR);
            ConversionResponse response = service.convert(request);

            assertEquals(100.0, response.amount());
            assertEquals("USD", response.from());
            assertEquals("EUR", response.to());
            assertEquals(92.0, response.result(), 0.01);
            assertEquals(0.92, response.rate(), 0.001);
        }

        @Test
        @DisplayName("convert should return correct response for EUR to USD")
        void convertShouldReturnCorrectResponseForEurToUsd() {
            ConversionRequest request = new ConversionRequest(100.0, Currency.EUR, Currency.USD);
            ConversionResponse response = service.convert(request);

            // EUR to USD: 100 * (1.0 / 0.92) = 108.70
            assertEquals(100.0, response.amount());
            assertEquals("EUR", response.from());
            assertEquals("USD", response.to());
            assertEquals(108.7, response.result(), 0.01);
            assertEquals(1.087, response.rate(), 0.001);
        }

        @Test
        @DisplayName("convert should return same currency with 1:1 rate")
        void convertShouldReturnSameCurrencyWithOneToOneRate() {
            ConversionRequest request = new ConversionRequest(100.0, Currency.USD, Currency.USD);
            ConversionResponse response = service.convert(request);

            assertEquals(100.0, response.amount());
            assertEquals("USD", response.from());
            assertEquals("USD", response.to());
            assertEquals(100.0, response.result());
            assertEquals(1.0, response.rate());
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("convert should handle zero amount")
        void convertShouldHandleZeroAmount() {
            ConversionRequest request = new ConversionRequest(0.0, Currency.USD, Currency.EUR);
            ConversionResponse response = service.convert(request);

            assertEquals(0.0, response.amount());
            assertEquals(0.0, response.result());
        }

        @Test
        @DisplayName("convert should handle negative amount")
        void convertShouldHandleNegativeAmount() {
            ConversionRequest request = new ConversionRequest(-100.0, Currency.USD, Currency.EUR);
            ConversionResponse response = service.convert(request);

            assertEquals(-100.0, response.amount());
            assertEquals(-92.0, response.result(), 0.01);
        }

        @Test
        @DisplayName("convert should handle large amounts")
        void convertShouldHandleLargeAmounts() {
            ConversionRequest request = new ConversionRequest(1000000.0, Currency.JPY, Currency.GBP);
            ConversionResponse response = service.convert(request);

            assertEquals(1000000.0, response.amount());
            assertTrue(response.result() > 0);
        }
    }

    @Nested
    @DisplayName("All Currency Pair Tests")
    class AllCurrencyPairTests {

        @Test
        @DisplayName("convert should handle all currency pairs")
        void convertShouldHandleAllCurrencyPairs() {
            for (Currency from : Currency.values()) {
                for (Currency to : Currency.values()) {
                    ConversionRequest request = new ConversionRequest(100.0, from, to);
                    ConversionResponse response = service.convert(request);

                    assertNotNull(response);
                    assertEquals(100.0, response.amount());
                    assertEquals(from.name(), response.from());
                    assertEquals(to.name(), response.to());
                    assertTrue(response.result() >= 0);
                    assertTrue(response.rate() > 0);
                }
            }
        }

        @Test
        @DisplayName("convert should return correct rate for all pairs")
        void convertShouldReturnCorrectRateForAllPairs() {
            for (Currency from : Currency.values()) {
                for (Currency to : Currency.values()) {
                    ConversionRequest request = new ConversionRequest(1.0, from, to);
                    ConversionResponse response = service.convert(request);

                    if (from == to) {
                        assertEquals(1.0, response.rate());
                    } else {
                        assertTrue(response.rate() > 0);
                    }
                }
            }
        }
    }

    @Nested
    @DisplayName("Rounding Tests")
    class RoundingTests {

        @Test
        @DisplayName("convert should round result to 2 decimal places")
        void convertShouldRoundResultToTwoDecimalPlaces() {
            // USD to JPY: 1 * 149.50 = 149.5 - should round to 149.5
            ConversionRequest request = new ConversionRequest(1.0, Currency.USD, Currency.JPY);
            ConversionResponse response = service.convert(request);

            // The service uses Math.round(result * 100.0) / 100.0
            double result = response.result();
            assertEquals(result, Math.round(result * 100) / 100.0);
        }

        @Test
        @DisplayName("convert should round small amounts correctly")
        void convertShouldRoundSmallAmountsCorrectly() {
            // Small amount conversion
            ConversionRequest request = new ConversionRequest(0.01, Currency.USD, Currency.EUR);
            ConversionResponse response = service.convert(request);

            // 0.01 * 0.92 = 0.0092 -> rounds to 0.01
            assertEquals(0.01, response.result());
        }
    }

    @Nested
    @DisplayName("Rate Calculation Tests")
    class RateCalculationTests {

        @Test
        @DisplayName("getRate should return correct rate for USD to EUR")
        void getRateShouldReturnCorrectRateForUsdToEur() {
            // Using reflection to test private method
            java.lang.reflect.Method method;
            try {
                method = ExchangeRateService.class.getDeclaredMethod("getRate", Currency.class, Currency.class);
                method.setAccessible(true);
                double rate = (double) method.invoke(service, Currency.USD, Currency.EUR);
                assertEquals(0.92, rate, 0.001);
            } catch (Exception e) {
                fail("Should not throw exception: " + e.getMessage());
            }
        }

        @Test
        @DisplayName("getRate should return 1.0 for same currency")
        void getRateShouldReturnOneForSameCurrency() {
            try {
                java.lang.reflect.Method method = ExchangeRateService.class.getDeclaredMethod("getRate", Currency.class, Currency.class);
                method.setAccessible(true);
                double rate = (double) method.invoke(service, Currency.USD, Currency.USD);
                assertEquals(1.0, rate, 0.001);
            } catch (Exception e) {
                fail("Should not throw exception: " + e.getMessage());
            }
        }

        @Test
        @DisplayName("getRate should return correct cross rate")
        void getRateShouldReturnCorrectCrossRate() {
            try {
                java.lang.reflect.Method method = ExchangeRateService.class.getDeclaredMethod("getRate", Currency.class, Currency.class);
                method.setAccessible(true);

                // EUR to GBP: (0.79 / 0.92) = 0.8587
                double rate = (double) method.invoke(service, Currency.EUR, Currency.GBP);
                assertEquals(0.79 / 0.92, rate, 0.001);
            } catch (Exception e) {
                fail("Should not throw exception: " + e.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("Response Consistency Tests")
    class ResponseConsistencyTests {

        @Test
        @DisplayName("result should equal amount multiplied by rate")
        void resultShouldEqualAmountMultipliedByRate() {
            for (Currency from : Currency.values()) {
                for (Currency to : Currency.values()) {
                    ConversionRequest request = new ConversionRequest(100.0, from, to);
                    ConversionResponse response = service.convert(request);

                    double expectedResult = 100.0 * response.rate();
                    // Account for rounding
                    assertEquals(Math.round(expectedResult * 100.0) / 100.0, response.result(), 0.01);
                }
            }
        }
    }
}