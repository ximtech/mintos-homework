package mintos.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.math.BigDecimal;
import java.util.UUID;

public record ClientAccountDTO(
        @JsonProperty("account_id") UUID id,
        @JsonProperty("client_id") UUID clientId,
        @JsonProperty("currency") String currency,
        @JsonProperty("balance") BigDecimal balance) {
}
