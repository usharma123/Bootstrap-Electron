package com.example.currency.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Currency conversion response")
public record ConversionResponse(
        @Schema(description = "Original amount", example = "100.0")
        double amount,

        @Schema(description = "Source currency code", example = "USD")
        String from,

        @Schema(description = "Target currency code", example = "EUR")
        String to,

        @Schema(description = "Converted amount", example = "92.50")
        double result,

        @Schema(description = "Exchange rate used", example = "0.925")
        double rate
) {
}
