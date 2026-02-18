package com.example.currency.service;

import com.example.currency.model.ConversionRequest;
import com.example.currency.model.ConversionResponse;
import com.example.currency.model.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ExchangeRateService Tests")
class ExchangeRateServiceTest {

    private ExchangeRateService exchangeRateService;

    @BeforeEach
    void setUp() {
        exchangeRateService = new ExchangeRateService();
    }

    @Test
    @DisplayName("Service should be created")
    void serviceShouldBeCreated() {
        assertNotNull(exchangeRateService);
    }

    @Test
    @DisplayName("Convert USD to EUR should return correct result")
    void convertUsdToEurShouldReturnCorrectResult() {
        ConversionRequest request = new ConversionRequest(100.0, Currency.USD, Currency.EUR);
        ConversionResponse response = exchangeRateService.convert(request);

        assertEquals(100.0, response.amount());
        assertEquals("USD", response.from());
        assertEquals("EUR", response.to());
        assertEquals(92.0, response.result());
        assertEquals(0.92, response.rate());
    }

    @Test
    @DisplayName("Convert EUR to USD should return correct result")
    void convertEurToUsdShouldReturnCorrectResult() {
        ConversionRequest request = new ConversionRequest(100.0, Currency.EUR, Currency.USD);
        ConversionResponse response = exchangeRateService.convert(request);

        assertEquals(100.0, response.amount());
        assertEquals("EUR", response.from());
        assertEquals("USD", response.to());
        assertEquals(108.70, response.result(), 0.01);
    }

    @Test
    @DisplayName("Convert same currency should return 1:1 rate")
    void convertSameCurrencyShouldReturnOneToOneRate() {
        ConversionRequest request = new ConversionRequest(100.0, Currency.USD, Currency.USD);
        ConversionResponse response = exchangeRateService.convert(request);

        assertEquals(100.0, response.amount());
        assertEquals("USD", response.from());
        assertEquals("USD", response.to());
        assertEquals(100.0, response.result());
        assertEquals(1.0, response.rate());
    }

    @Test
    @DisplayName("Convert USD to JPY should return correct result")
    void convertUsdToJpyShouldReturnCorrectResult() {
        ConversionRequest request = new ConversionRequest(100.0, Currency.USD, Currency.JPY);
        ConversionResponse response = exchangeRateService.convert(request);

        assertEquals(100.0, response.amount());
        assertEquals("USD", response.from());
        assertEquals("JPY", response.to());
        assertEquals(14950.0, response.result());
        assertEquals(149.50, response.rate());
    }

    @Test
    @DisplayName("Convert JPY to USD should return correct result")
    void convertJpyToUsdShouldReturnCorrectResult() {
        ConversionRequest request = new ConversionRequest(14950.0, Currency.JPY, Currency.USD);
        ConversionResponse response = exchangeRateService.convert(request);

        assertEquals(14950.0, response.amount());
        assertEquals("JPY", response.from());
        assertEquals("USD", response.to());
        assertEquals(100.0, response.result(), 0.01);
    }

    @Test
    @DisplayName("Convert zero amount should return zero result")
    void convertZeroAmountShouldReturnZeroResult() {
        ConversionRequest request = new ConversionRequest(0.0, Currency.USD, Currency.EUR);
        ConversionResponse response = exchangeRateService.convert(request);

        assertEquals(0.0, response.amount());
        assertEquals(0.0, response.result());
    }

    @Test
    @DisplayName("Convert large amount should handle correctly")
    void convertLargeAmountShouldHandleCorrectly() {
        ConversionRequest request = new ConversionRequest(1000000.0, Currency.USD, Currency.EUR);
        ConversionResponse response = exchangeRateService.convert(request);

        assertEquals(1000000.0, response.amount());
        assertEquals(920000.0, response.result());
    }

    @Test
    @DisplayName("Convert GBP to CAD should return correct result")
    void convertGbpToCadShouldReturnCorrectResult() {
        ConversionRequest request = new ConversionRequest(100.0, Currency.GBP, Currency.CAD);
        ConversionResponse response = exchangeRateService.convert(request);

        assertEquals(100.0, response.amount());
        assertEquals("GBP", response.from());
        assertEquals("CAD", response.to());
        // GBP to USD: 0.79, USD to CAD: 1.35, rate = 1.35/0.79 = 1.70886
        assertEquals(170.89, response.result(), 0.01);
    }

    @Test
    @DisplayName("Convert AUD to CHF should return correct result")
    void convertAudToChfShouldReturnCorrectResult() {
        ConversionRequest request = new ConversionRequest(100.0, Currency.AUD, Currency.CHF);
        ConversionResponse response = exchangeRateService.convert(request);

        assertEquals(100.0, response.amount());
        assertEquals("AUD", response.from());
        assertEquals("CHF", response.to());
        // AUD to USD: 1.53, USD to CHF: 0.88, rate = 0.88/1.53 = 0.5752
        assertEquals(57.52, response.result(), 0.01);
    }

    @Test
    @DisplayName("Convert CNY to INR should return correct result")
    void convertCnyToInrShouldReturnCorrectResult() {
        ConversionRequest request = new ConversionRequest(100.0, Currency.CNY, Currency.INR);
        ConversionResponse response = exchangeRateService.convert(request);

        assertEquals(100.0, response.amount());
        assertEquals("CNY", response.from());
        assertEquals("INR", response.to());
        // CNY to USD: 7.24, USD to INR: 83.12, rate = 83.12/7.24
        // Using delta for floating point precision
        assertEquals(1148.07, response.result(), 0.01);
    }

    @Test
    @DisplayName("Convert MXN to USD should return correct result")
    void convertMxnToUsdShouldReturnCorrectResult() {
        ConversionRequest request = new ConversionRequest(100.0, Currency.MXN, Currency.USD);
        ConversionResponse response = exchangeRateService.convert(request);

        assertEquals(100.0, response.amount());
        assertEquals("MXN", response.from());
        assertEquals("USD", response.to());
        // MXN to USD: 17.15, rate = 1/17.15 = 0.0583
        assertEquals(5.83, response.result(), 0.01);
    }

    @Test
    @DisplayName("Result should be rounded to 2 decimal places")
    void resultShouldBeRoundedToTwoDecimalPlaces() {
        ConversionRequest request = new ConversionRequest(1.0, Currency.USD, Currency.EUR);
        ConversionResponse response = exchangeRateService.convert(request);

        // 1.0 * 0.92 = 0.92 (already 2 decimal places)
        assertEquals(0.92, response.result());
    }

    @Test
    @DisplayName("Response should contain correct from and to strings")
    void responseShouldContainCorrectFromAndToStrings() {
        ConversionRequest request = new ConversionRequest(100.0, Currency.GBP, Currency.JPY);
        ConversionResponse response = exchangeRateService.convert(request);

        assertEquals("GBP", response.from());
        assertEquals("JPY", response.to());
    }

    @Test
    @DisplayName("Rate should be consistent for same currency pair")
    void rateShouldBeConsistentForSameCurrencyPair() {
        ConversionRequest request1 = new ConversionRequest(100.0, Currency.USD, Currency.EUR);
        ConversionRequest request2 = new ConversionRequest(200.0, Currency.USD, Currency.EUR);

        ConversionResponse response1 = exchangeRateService.convert(request1);
        ConversionResponse response2 = exchangeRateService.convert(request2);

        assertEquals(response1.rate(), response2.rate(), 0.0001);
    }

    @Test
    @DisplayName("Convert negative amount should work correctly")
    void convertNegativeAmountShouldWorkCorrectly() {
        ConversionRequest request = new ConversionRequest(-100.0, Currency.USD, Currency.EUR);
        ConversionResponse response = exchangeRateService.convert(request);

        assertEquals(-100.0, response.amount());
        assertEquals(-92.0, response.result());
    }
}
