package mintos.homework.mapper;

import java.math.BigDecimal;
import java.util.UUID;
import javax.annotation.processing.Generated;
import mintos.homework.domain.TransactionStatus;
import mintos.homework.domain.entity.ClientAccount;
import mintos.homework.domain.entity.ClientTransaction;
import mintos.homework.dto.ClientTransactionDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-24T03:52:30+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class ClientTransactionMapperImpl implements ClientTransactionMapper {

    @Override
    public ClientTransactionDTO mapToTransaction(ClientTransaction entity) {
        if ( entity == null ) {
            return null;
        }

        UUID targetClientAccountId = null;
        TransactionStatus status = null;
        String currency = null;

        targetClientAccountId = entityTargetAccountId( entity );
        status = entity.getStatus();
        currency = entity.getCurrency();

        BigDecimal amountTransferred = null;

        ClientTransactionDTO clientTransactionDTO = new ClientTransactionDTO( targetClientAccountId, amountTransferred, status, currency );

        return clientTransactionDTO;
    }

    private UUID entityTargetAccountId(ClientTransaction clientTransaction) {
        if ( clientTransaction == null ) {
            return null;
        }
        ClientAccount targetAccount = clientTransaction.getTargetAccount();
        if ( targetAccount == null ) {
            return null;
        }
        UUID id = targetAccount.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
