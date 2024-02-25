package mintos.homework.mapper;

import mintos.homework.domain.entity.ClientTransaction;
import mintos.homework.dto.ClientTransactionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ClientTransactionMapper {

    @Mapping(target = "targetClientAccountId", source = "targetAccount.id")
    @Mapping(target = "amountTransferred", source = "amount")
    ClientTransactionDTO mapToTransaction(ClientTransaction entity);

}
