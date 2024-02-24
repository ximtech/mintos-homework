package mintos.homework.mapper;

import java.math.BigDecimal;
import java.util.UUID;
import javax.annotation.processing.Generated;
import mintos.homework.domain.entity.ClientAccount;
import mintos.homework.dto.ClientAccountDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-24T03:52:30+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class ClientAccountMapperImpl implements ClientAccountMapper {

    @Override
    public ClientAccountDTO mapToClientAccount(ClientAccount entity) {
        if ( entity == null ) {
            return null;
        }

        UUID id = null;
        UUID clientId = null;
        String currency = null;
        BigDecimal balance = null;

        id = entity.getId();
        clientId = entity.getClientId();
        currency = entity.getCurrency();
        balance = entity.getBalance();

        ClientAccountDTO clientAccountDTO = new ClientAccountDTO( id, clientId, currency, balance );

        return clientAccountDTO;
    }
}
