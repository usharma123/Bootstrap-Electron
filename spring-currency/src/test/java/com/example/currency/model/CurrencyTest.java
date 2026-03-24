package com.example.currency.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Currency Enum Tests")
class CurrencyTest {

    @Nested
    @DisplayName("Enum Values Test")
    class EnumValuesTest {

        @Test
        @DisplayName("Should have exactly 10 currency values")
        void shouldHaveTenCurrencyValues() {
            assertEquals(10, Currency.values().length,
                "Currency enum should have exactly 10 currency codes");
        }

        @ParameterizedTest
        @EnumSource(Currency.class)
        @DisplayName("All enum values should not be null")
        void allEnumValuesShouldNotBeNull(Currency currency) {
            assertNotNull(currency);
        }
    }

    @Nested
    @DisplayName("ValueOf Tests")
    class ValueOfTests {

        @Test
        @DisplayName("valueOf should return correct currency for valid USD")
        void valueOfShouldReturnCorrectCurrencyForUsd() {
            assertEquals(Currency.USD, Currency.valueOf("USD"));
        }

        @Test
        @DisplayName("valueOf should return correct currency for valid EUR")
        void valueOfShouldReturnCorrectCurrencyForEur() {
            assertEquals(Currency.EUR, Currency.valueOf("EUR"));
        }

        @Test
        @DisplayName("valueOf should return correct currency for valid GBP")
        void valueOfShouldReturnCorrectCurrencyForGbp() {
            assertEquals(Currency.GBP, Currency.valueOf("GBP"));
        }

        @Test
        @DisplayName("valueOf should return correct currency for valid JPY")
        void valueOfShouldReturnCorrectCurrencyForJpy() {
            assertEquals(Currency.JPY, Currency.valueOf("JPY"));
        }

        @Test
        @DisplayName("valueOf should return correct currency for valid CAD")
        void valueOfShouldReturnCorrectCurrencyForCad() {
            assertEquals(Currency.CAD, Currency.valueOf("CAD"));
        }

        @Test
        @DisplayName("valueOf should return correct currency for valid AUD")
        void valueOfShouldReturnCorrectCurrencyForAud() {
            assertEquals(Currency.AUD, Currency.valueOf("AUD"));
        }

        @Test
        @DisplayName("valueOf should return correct currency for valid CHF")
        void valueOfShouldReturnCorrectCurrencyForChf() {
            assertEquals(Currency.CHF, Currency.valueOf("CHF"));
        }

        @Test
        @DisplayName("valueOf should return correct currency for valid CNY")
        void valueOfShouldReturnCorrectCurrencyForCny() {
            assertEquals(Currency.CNY, Currency.valueOf("CNY"));
        }

        @Test
        @DisplayName("valueOf should return correct currency for valid INR")
        void valueOfShouldReturnCorrectCurrencyForInr() {
            assertEquals(Currency.INR, Currency.valueOf("INR"));
        }

        @Test
        @DisplayName("valueOf should return correct currency for valid MXN")
        void valueOfShouldReturnCorrectCurrencyForMxn() {
            assertEquals(Currency.MXN, Currency.valueOf("MXN"));
        }

        @Test
        @DisplayName("valueOf should throw IllegalArgumentException for invalid currency")
        void valueOfShouldThrowForInvalidCurrency() {
            assertThrows(IllegalArgumentException.class,
                () -> Currency.valueOf("INVALID"));
        }

        @Test
        @DisplayName("valueOf should throw IllegalArgumentException for lowercase currency")
        void valueOfShouldThrowForLowercaseCurrency() {
            assertThrows(IllegalArgumentException.class,
                () -> Currency.valueOf("usd"));
        }
    }

    @Nested
    @DisplayName("Name Method Tests")
    class NameMethodTests {

        @Test
        @DisplayName("name() should return correct string for USD")
        void nameShouldReturnCorrectStringForUsd() {
            assertEquals("USD", Currency.USD.name());
        }

        @Test
        @DisplayName("name() should return correct string for EUR")
        void nameShouldReturnCorrectStringForEur() {
            assertEquals("EUR", Currency.EUR.name());
        }

        @Test
        @DisplayName("name() should return correct string for all currencies")
        void nameShouldReturnCorrectStringForAllCurrencies() {
            for (Currency currency : Currency.values()) {
                assertNotNull(currency.name());
                assertFalse(currency.name().isEmpty());
            }
        }
    }

    @Nested
    @DisplayName("Ordinal Method Tests")
    class OrdinalMethodTests {

        @Test
        @DisplayName("ordinal() should return unique values for each currency")
        void ordinalShouldReturnUniqueValues() {
            Currency[] values = Currency.values();
            long uniqueOrdinals = java.util.Arrays.stream(values)
                .mapToInt(Currency::ordinal)
                .distinct()
                .count();
            assertEquals(values.length, uniqueOrdinals);
        }

        @Test
        @DisplayName("ordinal() for USD should be 0")
        void ordinalForUsdShouldBeZero() {
            assertEquals(0, Currency.USD.ordinal());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Same currency should be equal")
        void sameCurrencyShouldBeEqual() {
            Currency usd1 = Currency.valueOf("USD");
            Currency usd2 = Currency.valueOf("USD");
            assertEquals(usd1, usd2);
        }

        @Test
        @DisplayName("Same currency should have same hashCode")
        void sameCurrencyShouldHaveSameHashCode() {
            Currency usd1 = Currency.valueOf("USD");
            Currency usd2 = Currency.valueOf("USD");
            assertEquals(usd1.hashCode(), usd2.hashCode());
        }

        @Test
        @DisplayName("Different currencies should not be equal")
        void differentCurrenciesShouldNotBeEqual() {
            assertNotEquals(Currency.USD, Currency.EUR);
        }
    }
}