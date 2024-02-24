package mintos.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.UUID;

public record FundTransactionResponseDTO(
        @JsonProperty("message") String message) {
}
