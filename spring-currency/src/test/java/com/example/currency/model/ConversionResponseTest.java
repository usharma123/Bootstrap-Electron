package com.example.currency.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ConversionResponse Record Tests")
class ConversionResponseTest {

    @Test
    @DisplayName("Should create record with correct values")
    void shouldCreateWithCorrectValues() {
        ConversionResponse response = new ConversionResponse(100.0, "USD", "EUR", 92.0, 0.92);

        assertEquals(100.0, response.amount());
        assertEquals("USD", response.from());
        assertEquals("EUR", response.to());
        assertEquals(92.0, response.result());
        assertEquals(0.92, response.rate());
    }

    @Test
    @DisplayName("Should handle zero values")
    void shouldHandleZeroValues() {
        ConversionResponse response = new ConversionResponse(0.0, "USD", "USD", 0.0, 1.0);

        assertEquals(0.0, response.amount());
        assertEquals(0.0, response.result());
    }

    @Test
    @DisplayName("Should handle large values")
    void shouldHandleLargeValues() {
        ConversionResponse response = new ConversionResponse(1_000_000.0, "USD", "JPY", 149_500_000.0, 149.5);

        assertEquals(1_000_000.0, response.amount());
        assertEquals(149_500_000.0, response.result());
    }

    @Test
    @DisplayName("Should handle fractional values")
    void shouldHandleFractionalValues() {
        ConversionResponse response = new ConversionResponse(0.01, "USD", "EUR", 0.01, 0.92);

        assertEquals(0.01, response.amount());
        assertEquals(0.01, response.result());
    }

    @Test
    @DisplayName("Should handle negative values")
    void shouldHandleNegativeValues() {
        ConversionResponse response = new ConversionResponse(-100.0, "USD", "EUR", -92.0, 0.92);

        assertEquals(-100.0, response.amount());
        assertEquals(-92.0, response.result());
    }

    @Test
    @DisplayName("Should handle same currency conversion")
    void shouldHandleSameCurrency() {
        ConversionResponse response = new ConversionResponse(100.0, "USD", "USD", 100.0, 1.0);

        assertEquals(100.0, response.result());
        assertEquals(1.0, response.rate());
    }

    @Test
    @DisplayName("equals should return true for same values")
    void equals_SameValues() {
        ConversionResponse response1 = new ConversionResponse(100.0, "USD", "EUR", 92.0, 0.92);
        ConversionResponse response2 = new ConversionResponse(100.0, "USD", "EUR", 92.0, 0.92);

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    @DisplayName("equals should return false for different amounts")
    void equals_DifferentAmounts() {
        ConversionResponse response1 = new ConversionResponse(100.0, "USD", "EUR", 92.0, 0.92);
        ConversionResponse response2 = new ConversionResponse(200.0, "USD", "EUR", 184.0, 0.92);

        assertNotEquals(response1, response2);
    }

    @Test
    @DisplayName("equals should return false for different from currencies")
    void equals_DifferentFromCurrency() {
        ConversionResponse response1 = new ConversionResponse(100.0, "USD", "EUR", 92.0, 0.92);
        ConversionResponse response2 = new ConversionResponse(100.0, "GBP", "EUR", 86.0, 0.86);

        assertNotEquals(response1, response2);
    }

    @Test
    @DisplayName("equals should return false for different to currencies")
    void equals_DifferentToCurrency() {
        ConversionResponse response1 = new ConversionResponse(100.0, "USD", "EUR", 92.0, 0.92);
        ConversionResponse response2 = new ConversionResponse(100.0, "USD", "GBP", 79.0, 0.79);

        assertNotEquals(response1, response2);
    }

    @Test
    @DisplayName("equals should return false for different types")
    void equals_DifferentType() {
        ConversionResponse response = new ConversionResponse(100.0, "USD", "EUR", 92.0, 0.92);

        assertNotEquals(response, "not a ConversionResponse");
        assertNotEquals(response, null);
    }

    @Test
    @DisplayName("toString should contain all fields")
    void toString_ContainsAllFields() {
        ConversionResponse response = new ConversionResponse(100.0, "USD", "EUR", 92.0, 0.92);
        String str = response.toString();

        assertTrue(str.contains("100.0"));
        assertTrue(str.contains("USD"));
        assertTrue(str.contains("EUR"));
        assertTrue(str.contains("92.0"));
        assertTrue(str.contains("0.92"));
    }

    @Test
    @DisplayName("hashCode should be consistent for equal objects")
    void hashCode_Consistent() {
        ConversionResponse response1 = new ConversionResponse(100.0, "USD", "EUR", 92.0, 0.92);
        ConversionResponse response2 = new ConversionResponse(100.0, "USD", "EUR", 92.0, 0.92);

        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    @DisplayName("Accessor methods should return correct values")
    void accessorMethodsReturnCorrectValues() {
        ConversionResponse response = new ConversionResponse(100.0, "USD", "EUR", 92.0, 0.92);

        assertEquals(100.0, response.amount());
        assertEquals("USD", response.from());
        assertEquals("EUR", response.to());
        assertEquals(92.0, response.result());
        assertEquals(0.92, response.rate());
    }
}
