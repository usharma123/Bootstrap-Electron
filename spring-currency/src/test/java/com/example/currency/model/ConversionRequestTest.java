package com.example.currency.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ConversionRequest Record Tests")
class ConversionRequestTest {

    @Test
    @DisplayName("equals should return true for same values")
    void equals_SameValues() {
        ConversionRequest request1 = new ConversionRequest(100.0, Currency.USD, Currency.EUR);
        ConversionRequest request2 = new ConversionRequest(100.0, Currency.USD, Currency.EUR);

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    @DisplayName("equals should return false for different values")
    void equals_DifferentValues() {
        ConversionRequest request1 = new ConversionRequest(100.0, Currency.USD, Currency.EUR);
        ConversionRequest request2 = new ConversionRequest(200.0, Currency.USD, Currency.EUR);

        assertNotEquals(request1, request2);
    }

    @Test
    @DisplayName("equals should return false for different types")
    void equals_DifferentType() {
        ConversionRequest request = new ConversionRequest(100.0, Currency.USD, Currency.EUR);

        assertNotEquals(request, "not a ConversionRequest");
        assertNotEquals(request, null);
    }

    @Test
    @DisplayName("toString should contain all fields")
    void toString_ContainsAllFields() {
        ConversionRequest request = new ConversionRequest(100.0, Currency.USD, Currency.EUR);
        String str = request.toString();

        assertTrue(str.contains("100.0"));
        assertTrue(str.contains("USD"));
        assertTrue(str.contains("EUR"));
    }

    @Test
    @DisplayName("hashCode should be consistent for equal objects")
    void hashCode_Consistent() {
        ConversionRequest request1 = new ConversionRequest(100.0, Currency.USD, Currency.EUR);
        ConversionRequest request2 = new ConversionRequest(100.0, Currency.USD, Currency.EUR);

        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    @DisplayName("All currencies should be usable in request")
    void allCurrenciesUsable() {
        for (Currency from : Currency.values()) {
            for (Currency to : Currency.values()) {
                ConversionRequest request = new ConversionRequest(100.0, from, to);
                assertNotNull(request);
                assertEquals(from, request.from());
                assertEquals(to, request.to());
            }
        }
    }
}
