package com.example.currency.controller;

import com.example.currency.model.ConversionRequest;
import com.example.currency.model.ConversionResponse;
import com.example.currency.model.Currency;
import com.example.currency.service.ExchangeRateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurrencyController.class)
@DisplayName("CurrencyController Tests")
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExchangeRateService exchangeRateService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("POST /api/convert Tests")
    class ConvertEndpointTests {

        @Test
        @DisplayName("Should return 200 and conversion response for valid request")
        void shouldReturnConversionResponseForValidRequest() throws Exception {
            ConversionRequest request = new ConversionRequest(100.0, Currency.USD, Currency.EUR);
            ConversionResponse expectedResponse = new ConversionResponse(
                100.0, "USD", "EUR", 92.0, 0.92);

            when(exchangeRateService.convert(any(ConversionRequest.class)))
                .thenReturn(expectedResponse);

            mockMvc.perform(post("/api/convert")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.from").value("USD"))
                .andExpect(jsonPath("$.to").value("EUR"))
                .andExpect(jsonPath("$.result").value(92.0))
                .andExpect(jsonPath("$.rate").value(0.92));

            verify(exchangeRateService, times(1)).convert(any(ConversionRequest.class));
        }

        @Test
        @DisplayName("Should return 200 for same currency conversion")
        void shouldReturnOkForSameCurrencyConversion() throws Exception {
            ConversionRequest request = new ConversionRequest(100.0, Currency.USD, Currency.USD);
            ConversionResponse expectedResponse = new ConversionResponse(
                100.0, "USD", "USD", 100.0, 1.0);

            when(exchangeRateService.convert(any(ConversionRequest.class)))
                .thenReturn(expectedResponse);

            mockMvc.perform(post("/api/convert")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(100.0))
                .andExpect(jsonPath("$.rate").value(1.0));
        }

        @Test
        @DisplayName("Should return 200 for zero amount conversion")
        void shouldReturnOkForZeroAmount() throws Exception {
            ConversionRequest request = new ConversionRequest(0.0, Currency.USD, Currency.EUR);
            ConversionResponse expectedResponse = new ConversionResponse(
                0.0, "USD", "EUR", 0.0, 0.92);

            when(exchangeRateService.convert(any(ConversionRequest.class)))
                .thenReturn(expectedResponse);

            mockMvc.perform(post("/api/convert")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(0.0))
                .andExpect(jsonPath("$.result").value(0.0));
        }

        @Test
        @DisplayName("Should call service exactly once")
        void shouldCallServiceExactlyOnce() throws Exception {
            ConversionRequest request = new ConversionRequest(50.0, Currency.GBP, Currency.INR);
            ConversionResponse expectedResponse = new ConversionResponse(
                50.0, "GBP", "INR", 5261.52, 105.2304);

            when(exchangeRateService.convert(any(ConversionRequest.class)))
                .thenReturn(expectedResponse);

            mockMvc.perform(post("/api/convert")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

            verify(exchangeRateService, times(1)).convert(any(ConversionRequest.class));
        }

        @Test
        @DisplayName("Should return 400 for invalid JSON body")
        void shouldReturn400ForInvalidJsonBody() throws Exception {
            mockMvc.perform(post("/api/convert")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("invalid json"))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 415 for unsupported content type")
        void shouldReturn415ForUnsupportedContentType() throws Exception {
            mockMvc.perform(post("/api/convert")
                    .contentType(MediaType.TEXT_PLAIN)
                    .content("100 USD EUR"))
                .andExpect(status().isUnsupportedMediaType());
        }
    }
}