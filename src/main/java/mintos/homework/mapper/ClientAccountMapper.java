package mintos.homework.mapper;

import mintos.homework.domain.entity.ClientAccount;
import mintos.homework.dto.ClientAccountDTO;
import org.mapstruct.Mapper;

@Mapper
public interface ClientAccountMapper {
    
    ClientAccountDTO mapToClientAccount(ClientAccount entity);

}
