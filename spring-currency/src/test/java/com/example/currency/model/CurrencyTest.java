package com.example.currency.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Currency Enum Tests")
class CurrencyTest {

    @Test
    @DisplayName("All currency values should exist")
    void allCurrenciesShouldExist() {
        assertEquals(10, Currency.values().length);
        assertNotNull(Currency.valueOf("USD"));
        assertNotNull(Currency.valueOf("EUR"));
        assertNotNull(Currency.valueOf("GBP"));
        assertNotNull(Currency.valueOf("JPY"));
        assertNotNull(Currency.valueOf("CAD"));
        assertNotNull(Currency.valueOf("AUD"));
        assertNotNull(Currency.valueOf("CHF"));
        assertNotNull(Currency.valueOf("CNY"));
        assertNotNull(Currency.valueOf("INR"));
        assertNotNull(Currency.valueOf("MXN"));
    }

    @Test
    @DisplayName("valueOf should return correct currency for valid input")
    void valueOfShouldReturnCorrectCurrency() {
        assertEquals(Currency.USD, Currency.valueOf("USD"));
        assertEquals(Currency.EUR, Currency.valueOf("EUR"));
        assertEquals(Currency.GBP, Currency.valueOf("GBP"));
        assertEquals(Currency.JPY, Currency.valueOf("JPY"));
        assertEquals(Currency.CAD, Currency.valueOf("CAD"));
        assertEquals(Currency.AUD, Currency.valueOf("AUD"));
        assertEquals(Currency.CHF, Currency.valueOf("CHF"));
        assertEquals(Currency.CNY, Currency.valueOf("CNY"));
        assertEquals(Currency.INR, Currency.valueOf("INR"));
        assertEquals(Currency.MXN, Currency.valueOf("MXN"));
    }

    @Test
    @DisplayName("valueOf should throw IllegalArgumentException for invalid input")
    void valueOfShouldThrowForInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> Currency.valueOf("INVALID"));
        assertThrows(IllegalArgumentException.class, () -> Currency.valueOf("usd"));
        assertThrows(IllegalArgumentException.class, () -> Currency.valueOf(""));
        assertThrows(IllegalArgumentException.class, () -> Currency.valueOf("ABC"));
    }

    @Test
    @DisplayName("name() should return correct string representation")
    void nameShouldReturnCorrectString() {
        assertEquals("USD", Currency.USD.name());
        assertEquals("EUR", Currency.EUR.name());
        assertEquals("GBP", Currency.GBP.name());
        assertEquals("JPY", Currency.JPY.name());
        assertEquals("CAD", Currency.CAD.name());
        assertEquals("AUD", Currency.AUD.name());
        assertEquals("CHF", Currency.CHF.name());
        assertEquals("CNY", Currency.CNY.name());
        assertEquals("INR", Currency.INR.name());
        assertEquals("MXN", Currency.MXN.name());
    }

    @Test
    @DisplayName("ordinal() should return correct index")
    void ordinalShouldReturnCorrectIndex() {
        assertEquals(0, Currency.USD.ordinal());
        assertEquals(1, Currency.EUR.ordinal());
        assertEquals(2, Currency.GBP.ordinal());
        assertEquals(3, Currency.JPY.ordinal());
        assertEquals(4, Currency.CAD.ordinal());
        assertEquals(5, Currency.AUD.ordinal());
        assertEquals(6, Currency.CHF.ordinal());
        assertEquals(7, Currency.CNY.ordinal());
        assertEquals(8, Currency.INR.ordinal());
        assertEquals(9, Currency.MXN.ordinal());
    }

    @Test
    @DisplayName("equals() should work correctly between currencies")
    void equalsShouldWorkCorrectly() {
        assertEquals(Currency.USD, Currency.USD);
        assertNotEquals(Currency.USD, Currency.EUR);
        assertNotEquals(Currency.USD, null);
        assertNotEquals(Currency.USD, "USD");
    }

    @Test
    @DisplayName("hashCode() should be consistent")
    void hashCodeShouldBeConsistent() {
        assertEquals(Currency.USD.hashCode(), Currency.USD.hashCode());
        assertEquals(Currency.EUR.hashCode(), Currency.EUR.hashCode());
    }

    @Test
    @DisplayName("toString() should return name")
    void toStringShouldReturnName() {
        assertEquals("USD", Currency.USD.toString());
        assertEquals("EUR", Currency.EUR.toString());
    }

    @Test
    @DisplayName("compareTo() should work correctly")
    void compareToShouldWorkCorrectly() {
        assertEquals(0, Currency.USD.compareTo(Currency.USD));
        assertTrue(Currency.USD.compareTo(Currency.EUR) < 0);
        assertTrue(Currency.EUR.compareTo(Currency.USD) > 0);
    }
}