package mintos.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.util.UUID;

public record FundTransactionRequestDTO(
        @NotNull(message = "From account id can't be null")
        @JsonProperty("from_account_id") 
        UUID fromAccountId,

        @NotNull(message = "To account id can't be null")
        @JsonProperty("to_account_id") 
        UUID toAccountId,
        
        @NotNull(message = "Amount can't be null")
        @DecimalMin(value = "0.0", inclusive = false)
        @JsonProperty("amount") 
        BigDecimal amount,
        
        @NotNull(message = "Currency name can't be null")
        @Pattern(regexp = "^[a-zA-Z]{3}$", message = "Invalid currency value")
        @JsonProperty("currency") 
        String currency) {
}
