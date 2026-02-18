package com.example.currency.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ConversionResponse Record Tests")
class ConversionResponseTest {

    @Test
    @DisplayName("Constructor should create valid object")
    void constructorShouldCreateValidObject() {
        ConversionResponse response = new ConversionResponse(100.0, "USD", "EUR", 92.0, 0.92);

        assertEquals(100.0, response.amount());
        assertEquals("USD", response.from());
        assertEquals("EUR", response.to());
        assertEquals(92.0, response.result());
        assertEquals(0.92, response.rate());
    }

    @Test
    @DisplayName("Constructor should handle edge case values")
    void constructorShouldHandleEdgeCaseValues() {
        ConversionResponse zeroResponse = new ConversionResponse(0.0, "USD", "EUR", 0.0, 0.92);
        assertEquals(0.0, zeroResponse.amount());
        assertEquals(0.0, zeroResponse.result());

        ConversionResponse largeValues = new ConversionResponse(Double.MAX_VALUE, "USD", "EUR", Double.MAX_VALUE * 0.92, 0.92);
        assertEquals(Double.MAX_VALUE, largeValues.amount());
    }

    @Test
    @DisplayName("equals() should work correctly for same values")
    void equalsShouldWorkCorrectlyForSameValues() {
        ConversionResponse response1 = new ConversionResponse(100.0, "USD", "EUR", 92.0, 0.92);
        ConversionResponse response2 = new ConversionResponse(100.0, "USD", "EUR", 92.0, 0.92);

        assertEquals(response1, response2);
    }

    @Test
    @DisplayName("equals() should return false for different values")
    void equalsShouldReturnFalseForDifferentValues() {
        ConversionResponse response1 = new ConversionResponse(100.0, "USD", "EUR", 92.0, 0.92);
        ConversionResponse response2 = new ConversionResponse(200.0, "USD", "EUR", 184.0, 0.92);
        ConversionResponse response3 = new ConversionResponse(100.0, "EUR", "USD", 92.0, 0.92);

        assertNotEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertNotEquals(response1, null);
        assertNotEquals(response1, "ConversionResponse(100.0, USD, EUR, 92.0, 0.92)");
    }

    @Test
    @DisplayName("hashCode() should be consistent")
    void hashCodeShouldBeConsistent() {
        ConversionResponse response1 = new ConversionResponse(100.0, "USD", "EUR", 92.0, 0.92);
        ConversionResponse response2 = new ConversionResponse(100.0, "USD", "EUR", 92.0, 0.92);

        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    @DisplayName("hashCode() should differ for different values")
    void hashCodeShouldDifferForDifferentValues() {
        ConversionResponse response1 = new ConversionResponse(100.0, "USD", "EUR", 92.0, 0.92);
        ConversionResponse response2 = new ConversionResponse(200.0, "USD", "EUR", 184.0, 0.92);

        assertNotEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    @DisplayName("toString() should contain all field values")
    void toStringShouldContainAllFieldValues() {
        ConversionResponse response = new ConversionResponse(100.0, "USD", "EUR", 92.0, 0.92);
        String str = response.toString();

        assertTrue(str.contains("100.0"));
        assertTrue(str.contains("USD"));
        assertTrue(str.contains("EUR"));
        assertTrue(str.contains("92.0"));
        assertTrue(str.contains("0.92"));
    }

    @Test
    @DisplayName("Accessor methods should work")
    void accessorMethodsShouldWork() {
        ConversionResponse response = new ConversionResponse(150.0, "GBP", "JPY", 22350.0, 149.0);

        assertEquals(150.0, response.amount());
        assertEquals("GBP", response.from());
        assertEquals("JPY", response.to());
        assertEquals(22350.0, response.result());
        assertEquals(149.0, response.rate());
    }

    @Test
    @DisplayName("Equals with itself should return true")
    void equalsWithItselfShouldReturnTrue() {
        ConversionResponse response = new ConversionResponse(100.0, "USD", "EUR", 92.0, 0.92);

        assertEquals(response, response);
    }

    @Test
    @DisplayName("hashCode should be same for same object")
    void hashCodeShouldBeSameForSameObject() {
        ConversionResponse response = new ConversionResponse(100.0, "USD", "EUR", 92.0, 0.92);

        assertEquals(response.hashCode(), response.hashCode());
    }
}