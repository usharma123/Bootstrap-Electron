package com.example.currency.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ConversionRequest Record Tests")
class ConversionRequestTest {

    @Test
    @DisplayName("Constructor should create valid object")
    void constructorShouldCreateValidObject() {
        ConversionRequest request = new ConversionRequest(100.0, Currency.USD, Currency.EUR);

        assertEquals(100.0, request.amount());
        assertEquals(Currency.USD, request.from());
        assertEquals(Currency.EUR, request.to());
    }

    @Test
    @DisplayName("Constructor should handle edge case values")
    void constructorShouldHandleEdgeCaseValues() {
        ConversionRequest zeroRequest = new ConversionRequest(0.0, Currency.USD, Currency.EUR);
        assertEquals(0.0, zeroRequest.amount());
        assertEquals(Currency.USD, zeroRequest.from());
        assertEquals(Currency.EUR, zeroRequest.to());

        ConversionRequest largeAmount = new ConversionRequest(Double.MAX_VALUE, Currency.JPY, Currency.CHF);
        assertEquals(Double.MAX_VALUE, largeAmount.amount());
    }

    @Test
    @DisplayName("equals() should work correctly for same values")
    void equalsShouldWorkCorrectlyForSameValues() {
        ConversionRequest request1 = new ConversionRequest(100.0, Currency.USD, Currency.EUR);
        ConversionRequest request2 = new ConversionRequest(100.0, Currency.USD, Currency.EUR);

        assertEquals(request1, request2);
    }

    @Test
    @DisplayName("equals() should return false for different values")
    void equalsShouldReturnFalseForDifferentValues() {
        ConversionRequest request1 = new ConversionRequest(100.0, Currency.USD, Currency.EUR);
        ConversionRequest request2 = new ConversionRequest(100.0, Currency.EUR, Currency.USD);
        ConversionRequest request3 = new ConversionRequest(200.0, Currency.USD, Currency.EUR);

        assertNotEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertNotEquals(request1, null);
        assertNotEquals(request1, "ConversionRequest(100.0, USD, EUR)");
    }

    @Test
    @DisplayName("hashCode() should be consistent")
    void hashCodeShouldBeConsistent() {
        ConversionRequest request1 = new ConversionRequest(100.0, Currency.USD, Currency.EUR);
        ConversionRequest request2 = new ConversionRequest(100.0, Currency.USD, Currency.EUR);

        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    @DisplayName("hashCode() should differ for different values")
    void hashCodeShouldDifferForDifferentValues() {
        ConversionRequest request1 = new ConversionRequest(100.0, Currency.USD, Currency.EUR);
        ConversionRequest request2 = new ConversionRequest(200.0, Currency.USD, Currency.EUR);

        assertNotEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    @DisplayName("toString() should contain all field values")
    void toStringShouldContainAllFieldValues() {
        ConversionRequest request = new ConversionRequest(100.0, Currency.USD, Currency.EUR);
        String str = request.toString();

        assertTrue(str.contains("100.0"));
        assertTrue(str.contains("USD"));
        assertTrue(str.contains("EUR"));
    }

    @Test
    @DisplayName("Accessor methods should work")
    void accessorMethodsShouldWork() {
        ConversionRequest request = new ConversionRequest(150.0, Currency.GBP, Currency.JPY);

        assertEquals(150.0, request.amount());
        assertEquals(Currency.GBP, request.from());
        assertEquals(Currency.JPY, request.to());
    }

    @Test
    @DisplayName("Same source and target currency should be valid")
    void sameSourceAndTargetShouldBeValid() {
        ConversionRequest request = new ConversionRequest(100.0, Currency.USD, Currency.USD);

        assertNotNull(request);
        assertEquals(100.0, request.amount());
        assertEquals(Currency.USD, request.from());
        assertEquals(Currency.USD, request.to());
    }
}