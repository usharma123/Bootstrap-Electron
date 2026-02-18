package com.example.currency.controller;

import com.example.currency.model.ConversionRequest;
import com.example.currency.model.ConversionResponse;
import com.example.currency.model.Currency;
import com.example.currency.service.ExchangeRateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("CurrencyController Tests")
class CurrencyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private CurrencyController controller;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    @DisplayName("Should return successful conversion response")
    void convert_Success() throws Exception {
        ConversionRequest request = new ConversionRequest(100.0, Currency.USD, Currency.EUR);
        ConversionResponse expectedResponse = new ConversionResponse(100.0, "USD", "EUR", 92.0, 0.92);

        when(exchangeRateService.convert(any(ConversionRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.from").value("USD"))
                .andExpect(jsonPath("$.to").value("EUR"))
                .andExpect(jsonPath("$.result").value(92.0))
                .andExpect(jsonPath("$.rate").value(0.92));

        verify(exchangeRateService).convert(any(ConversionRequest.class));
    }

    @Test
    @DisplayName("Should call service with correct request")
    void convert_PassesCorrectRequest() throws Exception {
        ConversionRequest request = new ConversionRequest(50.0, Currency.GBP, Currency.JPY);
        ConversionResponse response = new ConversionResponse(50.0, "GBP", "JPY", 9458.0, 189.16);

        when(exchangeRateService.convert(any(ConversionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(exchangeRateService).convert(request);
    }

    @Test
    @DisplayName("Should handle same currency conversion")
    void convert_SameCurrency() throws Exception {
        ConversionRequest request = new ConversionRequest(200.0, Currency.CAD, Currency.CAD);
        ConversionResponse response = new ConversionResponse(200.0, "CAD", "CAD", 200.0, 1.0);

        when(exchangeRateService.convert(any(ConversionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(200.0))
                .andExpect(jsonPath("$.rate").value(1.0));
    }

    @Test
    @DisplayName("Should return 200 for zero amount conversion")
    void convert_ZeroAmount() throws Exception {
        ConversionRequest request = new ConversionRequest(0.0, Currency.USD, Currency.EUR);
        ConversionResponse response = new ConversionResponse(0.0, "USD", "EUR", 0.0, 0.92);

        when(exchangeRateService.convert(any(ConversionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(0.0));
    }
}