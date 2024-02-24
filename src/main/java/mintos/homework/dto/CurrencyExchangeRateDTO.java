package mintos.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

public record CurrencyExchangeRateDTO(
        @JsonProperty("result") String result,
        @JsonProperty("base_code") String fromCurrency,
        @JsonProperty("conversion_rates")Map<String, BigDecimal> rateMap) {
}
