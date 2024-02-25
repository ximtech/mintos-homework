package mintos.homework.unit

import jakarta.validation.Validation
import mintos.homework.dto.FundTransactionRequestDTO
import spock.lang.Specification

class FundTransactionRequestSpec extends Specification {

    def validator = Validation.buildDefaultValidatorFactory().getValidator()

    def 'test FundTransactionRequestDTO - parameters validation'() {
        expect:
        validator.validate(new FundTransactionRequestDTO(fromAccountId, toAccountId, amount, currency)).size() == violations

        where:
        fromAccountId     | toAccountId       | amount | currency | violations
        UUID.randomUUID() | UUID.randomUUID() | 1.0    | 'USD'    | 0
        UUID.randomUUID() | UUID.randomUUID() | 1.0    | 'eur'    | 0
        UUID.randomUUID() | UUID.randomUUID() | 1.0    | 'eUr'    | 0
        UUID.randomUUID() | UUID.randomUUID() | 1.0    | 'EU'     | 1
        UUID.randomUUID() | UUID.randomUUID() | 1.0    | 'EURO'   | 1
        UUID.randomUUID() | UUID.randomUUID() | 0.0    | 'USD'    | 1
        UUID.randomUUID() | UUID.randomUUID() | -1.0   | 'USD'    | 1
        UUID.randomUUID() | UUID.randomUUID() | 0.0    | null     | 2
        UUID.randomUUID() | UUID.randomUUID() | null   | 'USD'    | 1
        null              | UUID.randomUUID() | 1.0    | 'USD'    | 1
        UUID.randomUUID() | null              | 1.0    | 'USD'    | 1
        null              | null              | null   | null     | 4
    }
}
