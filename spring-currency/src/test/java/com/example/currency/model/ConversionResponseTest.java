package com.example.currency.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ConversionResponse Record Tests")
class ConversionResponseTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor should create response with valid values")
        void constructorShouldCreateResponseWithValidValues() {
            ConversionResponse response = new ConversionResponse(
                100.0, "USD", "EUR", 92.0, 0.92);

            assertEquals(100.0, response.amount());
            assertEquals("USD", response.from());
            assertEquals("EUR", response.to());
            assertEquals(92.0, response.result());
            assertEquals(0.92, response.rate());
        }

        @Test
        @DisplayName("Constructor should handle zero values")
        void constructorShouldHandleZeroValues() {
            ConversionResponse response = new ConversionResponse(
                0.0, "USD", "EUR", 0.0, 0.0);

            assertEquals(0.0, response.amount());
            assertEquals(0.0, response.result());
            assertEquals(0.0, response.rate());
        }

        @Test
        @DisplayName("Constructor should handle negative values")
        void constructorShouldHandleNegativeValues() {
            ConversionResponse response = new ConversionResponse(
                -50.0, "USD", "EUR", -46.0, 0.92);

            assertEquals(-50.0, response.amount());
            assertEquals(-46.0, response.result());
        }

        @Test
        @DisplayName("Constructor should handle large values")
        void constructorShouldHandleLargeValues() {
            ConversionResponse response = new ConversionResponse(
                999999.99, "JPY", "GBP", 5236.84, 0.005237);

            assertEquals(999999.99, response.amount());
            assertEquals(5236.84, response.result());
        }

        @Test
        @DisplayName("Constructor should handle same currency conversion")
        void constructorShouldHandleSameCurrencyConversion() {
            ConversionResponse response = new ConversionResponse(
                100.0, "USD", "USD", 100.0, 1.0);

            assertEquals(100.0, response.amount());
            assertEquals("USD", response.from());
            assertEquals("USD", response.to());
            assertEquals(100.0, response.result());
            assertEquals(1.0, response.rate());
        }

        @Test
        @DisplayName("Constructor should handle all currency strings")
        void constructorShouldHandleAllCurrencyStrings() {
            for (Currency currency : Currency.values()) {
                ConversionResponse response = new ConversionResponse(
                    1.0, currency.name(), Currency.EUR.name(), 0.92, 0.92);

                assertEquals(currency.name(), response.from());
                assertEquals(Currency.EUR.name(), response.to());
            }
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Equal responses should be equal")
        void equalResponsesShouldBeEqual() {
            ConversionResponse response1 = new ConversionResponse(
                100.0, "USD", "EUR", 92.0, 0.92);
            ConversionResponse response2 = new ConversionResponse(
                100.0, "USD", "EUR", 92.0, 0.92);

            assertEquals(response1, response2);
        }

        @Test
        @DisplayName("Equal responses should have same hashCode")
        void equalResponsesShouldHaveSameHashCode() {
            ConversionResponse response1 = new ConversionResponse(
                100.0, "USD", "EUR", 92.0, 0.92);
            ConversionResponse response2 = new ConversionResponse(
                100.0, "USD", "EUR", 92.0, 0.92);

            assertEquals(response1.hashCode(), response2.hashCode());
        }

        @Test
        @DisplayName("Different amounts should not be equal")
        void differentAmountsShouldNotBeEqual() {
            ConversionResponse response1 = new ConversionResponse(
                100.0, "USD", "EUR", 92.0, 0.92);
            ConversionResponse response2 = new ConversionResponse(
                200.0, "USD", "EUR", 184.0, 0.92);

            assertNotEquals(response1, response2);
        }

        @Test
        @DisplayName("Different from currencies should not be equal")
        void differentFromCurrenciesShouldNotBeEqual() {
            ConversionResponse response1 = new ConversionResponse(
                100.0, "USD", "EUR", 92.0, 0.92);
            ConversionResponse response2 = new ConversionResponse(
                100.0, "EUR", "EUR", 100.0, 1.0);

            assertNotEquals(response1, response2);
        }

        @Test
        @DisplayName("Different to currencies should not be equal")
        void differentToCurrenciesShouldNotBeEqual() {
            ConversionResponse response1 = new ConversionResponse(
                100.0, "USD", "EUR", 92.0, 0.92);
            ConversionResponse response2 = new ConversionResponse(
                100.0, "USD", "GBP", 79.0, 0.79);

            assertNotEquals(response1, response2);
        }

        @Test
        @DisplayName("Different results should not be equal")
        void differentResultsShouldNotBeEqual() {
            ConversionResponse response1 = new ConversionResponse(
                100.0, "USD", "EUR", 92.0, 0.92);
            ConversionResponse response2 = new ConversionResponse(
                100.0, "USD", "EUR", 91.0, 0.91);

            assertNotEquals(response1, response2);
        }

        @Test
        @DisplayName("Different rates should not be equal")
        void differentRatesShouldNotBeEqual() {
            ConversionResponse response1 = new ConversionResponse(
                100.0, "USD", "EUR", 92.0, 0.92);
            ConversionResponse response2 = new ConversionResponse(
                100.0, "USD", "EUR", 92.0, 0.91);

            assertNotEquals(response1, response2);
        }

        @Test
        @DisplayName("Response should not be equal to null")
        void responseShouldNotBeEqualToNull() {
            ConversionResponse response = new ConversionResponse(
                100.0, "USD", "EUR", 92.0, 0.92);

            assertNotEquals(null, response);
        }

        @Test
        @DisplayName("Response should not be equal to different type")
        void responseShouldNotBeEqualToDifferentType() {
            ConversionResponse response = new ConversionResponse(
                100.0, "USD", "EUR", 92.0, 0.92);

            assertNotEquals("string", response);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("toString should include all fields")
        void toStringShouldIncludeAllFields() {
            ConversionResponse response = new ConversionResponse(
                100.0, "USD", "EUR", 92.0, 0.92);
            String str = response.toString();

            assertTrue(str.contains("100.0"));
            assertTrue(str.contains("USD"));
            assertTrue(str.contains("EUR"));
            assertTrue(str.contains("92.0"));
            assertTrue(str.contains("0.92"));
        }

        @Test
        @DisplayName("toString should not be empty")
        void toStringShouldNotBeEmpty() {
            ConversionResponse response = new ConversionResponse(
                100.0, "USD", "EUR", 92.0, 0.92);

            assertFalse(response.toString().isEmpty());
        }
    }
}