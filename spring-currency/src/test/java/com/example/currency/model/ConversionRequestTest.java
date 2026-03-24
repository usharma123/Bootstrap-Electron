package com.example.currency.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ConversionRequest Record Tests")
class ConversionRequestTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor should create request with valid values")
        void constructorShouldCreateRequestWithValidValues() {
            ConversionRequest request = new ConversionRequest(100.0, Currency.USD, Currency.EUR);

            assertEquals(100.0, request.amount());
            assertEquals(Currency.USD, request.from());
            assertEquals(Currency.EUR, request.to());
        }

        @Test
        @DisplayName("Constructor should handle zero amount")
        void constructorShouldHandleZeroAmount() {
            ConversionRequest request = new ConversionRequest(0.0, Currency.USD, Currency.EUR);

            assertEquals(0.0, request.amount());
            assertEquals(Currency.USD, request.from());
            assertEquals(Currency.EUR, request.to());
        }

        @Test
        @DisplayName("Constructor should handle negative amount")
        void constructorShouldHandleNegativeAmount() {
            ConversionRequest request = new ConversionRequest(-50.0, Currency.USD, Currency.EUR);

            assertEquals(-50.0, request.amount());
        }

        @Test
        @DisplayName("Constructor should handle large amount")
        void constructorShouldHandleLargeAmount() {
            ConversionRequest request = new ConversionRequest(999999.99, Currency.JPY, Currency.GBP);

            assertEquals(999999.99, request.amount());
        }

        @Test
        @DisplayName("Constructor should handle same source and target currency")
        void constructorShouldHandleSameSourceAndTargetCurrency() {
            ConversionRequest request = new ConversionRequest(100.0, Currency.USD, Currency.USD);

            assertEquals(100.0, request.amount());
            assertEquals(Currency.USD, request.from());
            assertEquals(Currency.USD, request.to());
        }

        @Test
        @DisplayName("Constructor should handle all currency pairs")
        void constructorShouldHandleAllCurrencyPairs() {
            for (Currency from : Currency.values()) {
                for (Currency to : Currency.values()) {
                    ConversionRequest request = new ConversionRequest(1.0, from, to);
                    assertEquals(from, request.from());
                    assertEquals(to, request.to());
                }
            }
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Equal requests should be equal")
        void equalRequestsShouldBeEqual() {
            ConversionRequest request1 = new ConversionRequest(100.0, Currency.USD, Currency.EUR);
            ConversionRequest request2 = new ConversionRequest(100.0, Currency.USD, Currency.EUR);

            assertEquals(request1, request2);
        }

        @Test
        @DisplayName("Equal requests should have same hashCode")
        void equalRequestsShouldHaveSameHashCode() {
            ConversionRequest request1 = new ConversionRequest(100.0, Currency.USD, Currency.EUR);
            ConversionRequest request2 = new ConversionRequest(100.0, Currency.USD, Currency.EUR);

            assertEquals(request1.hashCode(), request2.hashCode());
        }

        @Test
        @DisplayName("Different amounts should not be equal")
        void differentAmountsShouldNotBeEqual() {
            ConversionRequest request1 = new ConversionRequest(100.0, Currency.USD, Currency.EUR);
            ConversionRequest request2 = new ConversionRequest(200.0, Currency.USD, Currency.EUR);

            assertNotEquals(request1, request2);
        }

        @Test
        @DisplayName("Different from currencies should not be equal")
        void differentFromCurrenciesShouldNotBeEqual() {
            ConversionRequest request1 = new ConversionRequest(100.0, Currency.USD, Currency.EUR);
            ConversionRequest request2 = new ConversionRequest(100.0, Currency.EUR, Currency.EUR);

            assertNotEquals(request1, request2);
        }

        @Test
        @DisplayName("Different to currencies should not be equal")
        void differentToCurrenciesShouldNotBeEqual() {
            ConversionRequest request1 = new ConversionRequest(100.0, Currency.USD, Currency.EUR);
            ConversionRequest request2 = new ConversionRequest(100.0, Currency.USD, Currency.GBP);

            assertNotEquals(request1, request2);
        }

        @Test
        @DisplayName("Request should not be equal to null")
        void requestShouldNotBeEqualToNull() {
            ConversionRequest request = new ConversionRequest(100.0, Currency.USD, Currency.EUR);

            assertNotEquals(null, request);
        }

        @Test
        @DisplayName("Request should not be equal to different type")
        void requestShouldNotBeEqualToDifferentType() {
            ConversionRequest request = new ConversionRequest(100.0, Currency.USD, Currency.EUR);

            assertNotEquals("string", request);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("toString should include all fields")
        void toStringShouldIncludeAllFields() {
            ConversionRequest request = new ConversionRequest(100.0, Currency.USD, Currency.EUR);
            String str = request.toString();

            assertTrue(str.contains("100.0"));
            assertTrue(str.contains("USD"));
            assertTrue(str.contains("EUR"));
        }

        @Test
        @DisplayName("toString should not be empty")
        void toStringShouldNotBeEmpty() {
            ConversionRequest request = new ConversionRequest(100.0, Currency.USD, Currency.EUR);

            assertFalse(request.toString().isEmpty());
        }
    }
}