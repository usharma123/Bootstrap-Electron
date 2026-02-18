package com.example.currency.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Currency Enum Tests")
class CurrencyTest {

    @Test
    @DisplayName("Should have exactly 10 currency values")
    void shouldHaveTenCurrencies() {
        Currency[] currencies = Currency.values();
        assertEquals(10, currencies.length);
    }

    @Test
    @DisplayName("Should contain all expected currencies")
    void shouldContainExpectedCurrencies() {
        Set<String> currencyNames = Arrays.stream(Currency.values())
                .map(Currency::name)
                .collect(Collectors.toSet());

        assertTrue(currencyNames.contains("USD"));
        assertTrue(currencyNames.contains("EUR"));
        assertTrue(currencyNames.contains("GBP"));
        assertTrue(currencyNames.contains("JPY"));
        assertTrue(currencyNames.contains("CAD"));
        assertTrue(currencyNames.contains("AUD"));
        assertTrue(currencyNames.contains("CHF"));
        assertTrue(currencyNames.contains("CNY"));
        assertTrue(currencyNames.contains("INR"));
        assertTrue(currencyNames.contains("MXN"));
    }

    @Test
    @DisplayName("valueOf should return correct currency for valid name")
    void valueOf_ValidName() {
        assertEquals(Currency.USD, Currency.valueOf("USD"));
        assertEquals(Currency.EUR, Currency.valueOf("EUR"));
        assertEquals(Currency.GBP, Currency.valueOf("GBP"));
        assertEquals(Currency.JPY, Currency.valueOf("JPY"));
    }

    @Test
    @DisplayName("valueOf should throw IllegalArgumentException for invalid name")
    void valueOf_InvalidName() {
        assertThrows(IllegalArgumentException.class, () -> Currency.valueOf("INVALID"));
        assertThrows(IllegalArgumentException.class, () -> Currency.valueOf("usd"));
        assertThrows(IllegalArgumentException.class, () -> Currency.valueOf(""));
        assertThrows(IllegalArgumentException.class, () -> Currency.valueOf("USDOLLAR"));
    }

    @Test
    @DisplayName("ordinal should return correct positions")
    void ordinal_CorrectPositions() {
        assertEquals(0, Currency.USD.ordinal());
        assertEquals(1, Currency.EUR.ordinal());
        assertEquals(2, Currency.GBP.ordinal());
        assertEquals(3, Currency.JPY.ordinal());
    }

    @Test
    @DisplayName("compareTo should work correctly")
    void compareTo_CorrectOrdering() {
        assertTrue(Currency.USD.compareTo(Currency.EUR) < 0);
        assertTrue(Currency.EUR.compareTo(Currency.USD) > 0);
        assertEquals(0, Currency.USD.compareTo(Currency.USD));
    }

    @Test
    @DisplayName("name() should return correct string representation")
    void name_CorrectRepresentation() {
        assertEquals("USD", Currency.USD.name());
        assertEquals("EUR", Currency.EUR.name());
        assertEquals("GBP", Currency.GBP.name());
    }

    @Test
    @DisplayName("All currencies should have valid declarations")
    void allCurrenciesHaveValidDeclarations() {
        for (Currency currency : Currency.values()) {
            assertNotNull(currency);
            assertNotNull(currency.name());
            assertTrue(currency.name().length() >= 3);
        }
    }
}
