package mintos.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import mintos.homework.domain.TransactionStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record ClientTransactionDTO(
        @JsonProperty("target_account_id") UUID targetClientAccountId,
        @JsonProperty("amount") BigDecimal amountTransferred,
        @JsonProperty("transaction_status") TransactionStatus status,
        @JsonProperty("currency") String currency) {
}
