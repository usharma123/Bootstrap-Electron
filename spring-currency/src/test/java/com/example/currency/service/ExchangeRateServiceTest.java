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

}
